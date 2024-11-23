/***********************************************************************************
 * Copyright (c) 2024 Alireza Khodakarami (Jiraiyah)                               *
 * ------------------------------------------------------------------------------- *
 * MIT License                                                                     *
 * =============================================================================== *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 * ------------------------------------------------------------------------------- *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 * ------------------------------------------------------------------------------- *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 ***********************************************************************************/

package jiraiyah.jienergy;

import jiraiyah.jiralib.blockentity.NoScreenUpdatableBE;
import jiraiyah.jiralib.blockentity.UpdatableBE;
import jiraiyah.jiralib.interfaces.ISync;
import net.minecraft.block.entity.BlockEntity;
import team.reborn.energy.api.base.SimpleEnergyStorage;

/**
 * The {@code SyncingEnergyStorage} class extends {@code SimpleEnergyStorage} to provide
 * energy storage capabilities with synchronization between server and client.
 * It utilizes an {@code UpdatableBE} block entity to ensure that any changes
 * in energy storage are properly updated across the network.
 * @author TurtyWurty
 */
@SuppressWarnings("unused")
public class SyncedEnergyStorage extends SimpleEnergyStorage implements ISync
{
    /**
     * Represents the associated block entity for this energy storage system.
     *
     * <p>This block entity is responsible for managing the state and behavior
     * of the block within the Minecraft world. It interacts with the energy
     * storage system to synchronize energy levels and update the block's
     * state as needed.</p>
     *
     * <p>The block entity is expected to implement interfaces such as
     * {@link jiraiyah.jiralib.interfaces.ISync} to ensure proper synchronization
     * across the network. It may also extend classes like
     * {@link jiraiyah.jiralib.blockentity.UpdatableBE} or
     * {@link jiraiyah.jiralib.blockentity.NoScreenUpdatableBE} to provide
     * additional functionality.</p>
     *
     * <p>Note: This attribute is final, indicating that the block entity
     * reference is immutable once assigned.</p>
     */
    private final BlockEntity blockEntity;

    /**
     * Indicates whether the energy storage state has been modified and requires synchronization.
     *
     * <p>This flag is used to track changes in the energy storage system that need to be
     * communicated to the client or saved to persistent storage. When set to {@code true},
     * it signifies that the energy levels or related properties have changed since the last
     * update, and appropriate actions should be taken to ensure consistency.</p>
     *
     * <p>Typically, this attribute is set to {@code true} whenever an operation alters the
     * energy storage, such as energy input or output. It should be reset to {@code false}
     * after the necessary synchronization or saving operations are completed.</p>
     */
    private boolean isDirty = false;

    /**
     * Constructs a new {@code SyncingEnergyStorage} instance.
     *
     * @param blockEntity The {@link BlockEntity} associated with this energy storage.
     * @param capacity The total capacity of the energy storage.
     * @param maxInsert The maximum amount of energy that can be inserted per operation.
     * @param maxExtract The maximum amount of energy that can be extracted per operation.
     */
    public SyncedEnergyStorage(BlockEntity blockEntity, long capacity, long maxInsert, long maxExtract)
    {
        super(capacity, maxInsert, maxExtract);
        this.blockEntity = blockEntity;
    }

    /**
     * Synchronizes the energy storage state with the client and updates the associated block entity.
     *
     * <p>This method is responsible for ensuring that the current state of the energy storage
     * is accurately reflected on the client side. It performs necessary operations to update
     * the {@link BlockEntity} and any other components that rely on the energy storage data.</p>
     *
     * <p>Implementations should leverage the {@link ISync} interface to facilitate efficient
     * data synchronization across the network. This method is typically called whenever there
     * are changes in the energy storage that need to be communicated to the client, ensuring
     * consistency between the server and client states.</p>
     */
    @SuppressWarnings("DataFlowIssue")
    @Override
    public void sync()
    {
        if (this.isDirty && this.blockEntity != null && this.blockEntity.hasWorld() && !this.blockEntity.getWorld().isClient)
        {
            this.isDirty = false;
            if (this.blockEntity instanceof NoScreenUpdatableBE updatableBE)
                updatableBE.update();
            else
                this.blockEntity.markDirty();
        }
    }

    /**
     * This method is called when the changes to the energy storage are finalized.
     * It is responsible for performing necessary actions after energy storage
     * modifications are committed.
     *
     * <p>
     * The method first invokes the parent class's {@link SimpleEnergyStorage#onFinalCommit()}
     * to ensure that any inherited finalization logic is executed. After that, it checks if the
     * associated {@link BlockEntity} is an instance of {@code UpdatableBE}. If it is, the method
     * calls the {@link UpdatableBE#update()} method to synchronize the state. If it is not an
     * {@code UpdatableBE}, it calls {@link BlockEntity#markDirty()} to indicate that the block entity
     * requires an update in the game state.
     * </p>
     *
     * @see SimpleEnergyStorage#onFinalCommit()
     * @see UpdatableBE#update()
     * @see BlockEntity#markDirty()
     */
    @Override
    protected void onFinalCommit()
    {
        super.onFinalCommit();
        this.isDirty = true;
    }

    /**
     * Returns the {@link BlockEntity} associated with this energy storage.
     *
     * @return The block entity associated with this energy storage.
     */
    public BlockEntity getBlockEntity()
    {
        return this.blockEntity;
    }
}