package jiraiyah.jienergy;

import jiraiyah.jiralib.blockentity.UpdatableBE;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EnergyHelper
{
    /**
     * Simulates the insertion of a specified amount of energy into an `EnergyStorage`.
     * This method opens a nested transaction to determine the maximum amount of energy
     * that can be inserted without actually committing the transaction.
     *
     * @param storage The `EnergyStorage` into which energy is to be inserted.
     * @param amount  The amount of energy to attempt to insert.
     * @param outer   The outer transaction within which this simulation occurs.
     *
     * @return The maximum amount of energy that can be inserted.
     */
    public static long simulateInsertion(EnergyStorage storage, long amount, Transaction outer)
    {
        try (Transaction inner = outer.openNested())
        {
            long max = storage.insert(amount, inner);
            inner.abort();
            return max;
        }
    }

    /**
     * Spreads energy from a `SimpleEnergyStorage` to adjacent `EnergyStorage` instances in the world,
     * It checks each direction for available energy storage and attempts to distribute energy evenly.
     * If the energy amount changes, it updates the block entity state accordingly.
     *
     * @param world   The `World` in which the energy spreading occurs.
     * @param pos     The `BlockPos` of the `SimpleEnergyStorage`.
     * @param storage The `SimpleEnergyStorage` from which energy is spread.
     *
     * @author Modified by Jiraiyah
     */
    public void spread(BlockEntity blockEntity, World world, BlockPos pos, SimpleEnergyStorage storage)
    {
        spread(blockEntity, world, pos, null, storage);
    }

    /**
     * Spreads energy from a `SimpleEnergyStorage` to adjacent `EnergyStorage` instances in the world,
     * It checks each direction for available energy storage and attempts to distribute energy.
     * The distribution can be equal or max possible per side, depending on the `equalAmount` flag.
     * If the energy amount changes, it updates the block entity state accordingly.
     *
     * @param world   The `World` in which the energy spreading occurs.
     * @param pos     The `BlockPos` of the `SimpleEnergyStorage`.
     * @param storage The `SimpleEnergyStorage` from which energy is spread.
     *
     * @author Modified by Jiraiyah
     */
    public void spread(BlockEntity blockEntity, World world, BlockPos pos, SimpleEnergyStorage storage, boolean equalAmount)
    {
        spread(blockEntity, world, pos, null, storage, equalAmount);
    }

    /**
     * Spreads energy from a `SimpleEnergyStorage` to adjacent `EnergyStorage` instances in the world,
     * excluding specific positions defined in the exceptions set. It checks each direction for available
     * energy storage and attempts to distribute energy evenly. If the energy amount changes, it updates
     * the block entity state accordingly.
     *
     * @param world      The `World` in which the energy spreading occurs.
     * @param pos        The `BlockPos` of the `SimpleEnergyStorage`.
     * @param exceptions A set of `BlockPos` that should be excluded from energy spreading.
     * @param storage    The `SimpleEnergyStorage` from which energy is spread.
     *
     * @author Modified by Jiraiyah
     */
    public void spread(BlockEntity blockEntity, World world, BlockPos pos, Set<BlockPos> exceptions, SimpleEnergyStorage storage)
    {
        spread(blockEntity, world, pos, exceptions, storage, true);
    }

    /**
     * Spreads energy from a `SimpleEnergyStorage` to adjacent `EnergyStorage` instances in the world,
     * excluding specific positions defined in the exceptions set. It checks each direction for available
     * energy storage and attempts to distribute energy. The distribution can be equal or max possible per side,
     * depending on the `equalAmount` flag. If the energy amount changes, it updates
     * the block entity state accordingly.
     *
     * @param world      The `World` in which the energy spreading occurs.
     * @param pos        The `BlockPos` of the `SimpleEnergyStorage`.
     * @param exceptions A set of `BlockPos` that should be excluded from energy spreading.
     * @param storage    The `SimpleEnergyStorage` from which energy is spread.
     *
     * @author Modified by Jiraiyah
     */
    public void spread(BlockEntity blockEntity, World world, BlockPos pos, Set<BlockPos> exceptions, SimpleEnergyStorage storage, boolean equalAmount)
    {
        List<EnergyStorage> storages = new ArrayList<>();
        for (Direction direction : Direction.values())
        {
            BlockPos adjacentPos = pos.offset(direction);
            if (exceptions != null && exceptions.contains(adjacentPos))
                continue;

            EnergyStorage energyStorage = EnergyStorage.SIDED.find(world, adjacentPos, direction.getOpposite());
            if (energyStorage == null || !energyStorage.supportsInsertion() || energyStorage.getAmount() >= energyStorage.getCapacity())
                continue;
            storages.add(energyStorage);
        }

        if (storages.isEmpty())
            return;

        try (Transaction transaction = Transaction.openOuter())
        {
            long current = storage.getAmount();
            long totalExtractable = storage.extract(Long.MAX_VALUE, transaction);
            long totalInserted = 0;
            long finalAmount = equalAmount ? totalExtractable / storages.size() : totalExtractable;

            for (EnergyStorage energyStorage : storages)
            {
                long insertable = EnergyHelper.simulateInsertion(energyStorage, finalAmount, transaction);
                long inserted = energyStorage.insert(insertable, transaction);
                totalInserted += inserted;
            }

            if (totalInserted < totalExtractable)
                storage.amount += totalExtractable - totalInserted;

            transaction.commit();

            if (current != storage.getAmount())
            {
                if (blockEntity instanceof UpdatableBE updatableBE)
                    updatableBE.update();
                else if (blockEntity instanceof BlockEntity entity)
                    entity.markDirty();
            }
        }
    }
}