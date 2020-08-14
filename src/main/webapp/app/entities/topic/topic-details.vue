<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="topic">
                <h2 class="jh-entity-heading"><span>Topic details</span>
                
                    <div class="btn-group float-right">

                        <button type="submit"
                            v-on:click.prevent="previousState()"
                            class="btn btn-info float-right">
                            <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                        </button>

                        <router-link :to="{name: 'MessageCreate', params: {clusterDbId: topic.cluster.id, topicName: topic.name, clusterName: topic.cluster.name, clusterInternalId: topic.cluster.clusterId}}" 
                                    tag="button" class="btn btn-outline-success btn-sm details" :disabled="topic.isInternal">
                            <font-awesome-icon icon="paper-plane"></font-awesome-icon>
                            <span class="d-none d-md-inline">Produce</span>
                        </router-link>
                        <router-link :to="{name: 'Message', params: {clusterDbId: topic.cluster.id, topicName: topic.name}}" tag="button" class="btn btn-outline-secondary btn-sm details">
                            <font-awesome-icon icon="envelope-open-text"></font-awesome-icon>
                            <span class="d-none d-md-inline">Consume</span>
                        </router-link>
                        <router-link v-if="topic.cluster !== undefined" :to="{name: 'TopicEdit', params: {clusterDbId: topic.cluster.id, topicName: topic.name}}" tag="button" class="btn btn-primary float-right">
                            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                        </router-link>
                    </div>
                
                </h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Topic name</span>
                    </dt>
                    <dd>
                        <span>{{topic.name}}</span>
                    </dd>
                    <dt>
                        <span>Is Internal</span>
                    </dt>
                    <dd>
                        <span>{{isInternal()}}</span>
                    </dd>
                    <dt>
                        <span>Number of partitions</span>
                    </dt>
                    <dd>
                        <span>{{getPartitionsCount()}}</span>
                    </dd>
                    <dt>
                        <span>Cluster</span>
                    </dt>
                    <dd>
                        <div v-if="topic.cluster">
                            <router-link :to="{name: 'ClusterView', params: {clusterId: topic.cluster.id}}">{{topic.cluster.name}} ({{topic.cluster.clusterId}})</router-link>
                        </div>
                    </dd>
                </dl>

                <h4><span>Partitions</span></h4>
                <div class="table-responsive">
                    <table class="table table-sm table-striped table-bordered">
                        <thead>
                            <tr>
                                <th><span>Partition</span></th>
                                <th><span>Leader broker</span></th>
                                <th><span>Replica brokers</span></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="partition in topic.partitions" :key="partition.id">
                                
                                <td><span>{{topic.name}}-<b>{{partition.id}}</b></span></td>
                                <td>
                                    <span>
                                        <router-link :to="{name: 'BrokerView', params: {clusterId: topic.cluster.id, brokerId: partition.leader.brokerId}}">
                                            broker {{partition.leader.brokerId}} ({{partition.leader.host}}:{{partition.leader.port}}) 
                                        </router-link>
                                    </span>
                                </td>
                                <td>
                                    <span v-for="(replica, index) in partition.replicas" :key="replica.id">
                                       <small>
                                           <router-link :to="{name: 'BrokerView', params: {clusterId: topic.cluster.id, brokerId: replica.brokerId}}">
                                                broker {{replica.brokerId}}
                                            </router-link>
                                            <span v-if="index !== partition.replicas.length - 1">&#8226; </span>
                                       </small>
                                    </span>
                                </td>
                                <td>
                                    <router-link :to="{name: 'Message', params: {clusterDbId: topic.cluster.id, topicName: topic.name, partitionFilter: partition.id}}" tag="button" class="btn btn-outline-secondary btn-sm details float-right">
                                        <font-awesome-icon icon="envelope-open-text"></font-awesome-icon>
                                        <span class="d-none d-md-inline">Consume</span>
                                    </router-link>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>


                <h4><span> Topic Configurations</span></h4>
                <div class="table-responsive">
                    <table class="table table-sm table-striped table-bordered">
                        <thead>
                            <tr>
                                <th><span>Name</span></th>
                                <th><span>Value</span></th>
                                <th><span>Is readonly?</span></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="config in topic.configs" :key="config.name">
                                <td><span>{{config.name}}</span></td>
                                <td><span>{{config.value}}</span></td>
                                <td><span>{{isReadonly(config)}}</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>


                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="topic.cluster !== undefined" :to="{name: 'TopicEdit', params: {topicId: topic.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./topic-details.component.ts">
</script>
