/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tcse.microsvcdiagnoser.dto;


import java.util.Objects;

/**
 * @author emeroad
 */
/*
* pinpoint数据对象
* */
public class TransactionId {

    private String agentId;
    private final long agentStartTime;
    private final long transactionSequence;

    public TransactionId(String agentId, long agentStartTime, long transactionSequence) {
        this.agentId = Objects.requireNonNull(agentId, "agentId");
        this.agentStartTime = agentStartTime;
        this.transactionSequence = transactionSequence;
    }

    public TransactionId(long agentStartTime, long transactionSequence) {
        this.agentStartTime = agentStartTime;
        this.transactionSequence = transactionSequence;
    }
    
    public TransactionId(){
        this.agentStartTime = -111111111;
        this.transactionSequence = -111111111;
    }

    public String getAgentId() {
        return agentId;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public long getTransactionSequence() {
        return transactionSequence;
    }


    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionId that = (TransactionId) o;

        if (agentStartTime != that.agentStartTime) return false;
        if (transactionSequence != that.transactionSequence) return false;
        if (!agentId.equals(that.agentId)) return false;

        return true;
    }

    
    public int hashCode() {
        int result = agentId.hashCode();
        result = 31 * result + (int) (agentStartTime ^ (agentStartTime >>> 32));
        result = 31 * result + (int) (transactionSequence ^ (transactionSequence >>> 32));
        return result;
    }

    
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransactionId{");
        sb.append("agentId='").append(agentId).append('\'');
        sb.append(", agentStartTime=").append(agentStartTime);
        sb.append(", transactionSequence=").append(transactionSequence);
        sb.append('}');
        return sb.toString();
    }
}
