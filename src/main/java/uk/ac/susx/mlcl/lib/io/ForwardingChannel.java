/*
 * Copyright (c) 2011-2012, University of Sussex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of the University of Sussex nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.susx.mlcl.lib.io;

import uk.ac.susx.mlcl.lib.Checks;

import javax.annotation.CheckReturnValue;
import javax.annotation.WillClose;
import java.io.Flushable;
import java.io.IOException;
import java.nio.channels.Channel;

/**
 * A Channel adapter that forwards all events to an encapsulated inner instance.
 *
 * @param <T> the type of Channel being encapsulated
 * @author Hamish I A Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
@CheckReturnValue
public abstract class ForwardingChannel<T extends Channel> implements Channel, Flushable {

    private final T inner;

    public ForwardingChannel(T inner) {
        Checks.checkNotNull("inner", inner);
        this.inner = inner;
    }

    public final T getInner() {
        return inner;
    }

    @Override
    public boolean isOpen() {
        return inner.isOpen();
    }

    @Override
    @WillClose
    public void close() throws IOException {
        inner.close();
    }

    @Override
    public void flush() throws IOException {
        if (inner instanceof Flushable)
            ((Flushable) inner).flush();
    }

    protected boolean equals(ForwardingChannel<?> other) {
        return this.inner == other.inner || this.inner.equals(other.inner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return equals((ForwardingChannel) obj);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[inner=" + inner + ']';
    }

}
