/*
 * Copyright (c) 2010-2012, University of Sussex
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
 * POSSIBILITY OF SUCH DAMAGE.To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.susx.mlcl.byblo.io;

import java.io.IOException;
import uk.ac.susx.mlcl.lib.io.CountingObjectSource;
import uk.ac.susx.mlcl.lib.io.ObjectSource;

/**
 *
 * @param <T>
 * @author Hamish I A Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
public class WeightStatsObjectSource<T> extends CountingObjectSource<ObjectSource<Weighted<T>>, Weighted<T>> {

    private double weightMin = Double.POSITIVE_INFINITY;

    private double weightMax = Double.NEGATIVE_INFINITY;

    private double weightSum = 0;

    public WeightStatsObjectSource(ObjectSource<Weighted<T>> inner) {
        super(inner);
    }

    public double getWeightSum() {
        return weightSum;
    }

    public double getWeightMax() {
        return weightMax;
    }

    public double getWeightMin() {
        return weightMin;
    }

    public double getWeightRange() {
        return getWeightMax() - getWeightMin();
    }

    public double getWeightMean() {
        return getWeightSum() / getCount();
    }

    @Override
    public Weighted<T> read() throws IOException {
        Weighted<T> wt = super.read();
        weightSum += wt.weight();
        weightMax = Math.max(weightMax, wt.weight());
        weightMin = Math.min(weightMin, wt.weight());
        return wt;
    }

}