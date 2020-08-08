<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="cluster">
                <h2 class="jh-entity-heading"><span>Cluster</span> {{cluster.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Cluster Id</span>
                    </dt>
                    <dd>
                        <span>{{cluster.clusterId}}</span>
                    </dd>
                    <dt>
                        <span>Name</span>
                    </dt>
                    <dd>
                        <span>{{cluster.name}}</span>
                    </dd>
                    <dt>
                        <span>Total # of topics</span>
                    </dt>
                    <dd>
                        <span>{{getTopicsCount()}}</span>
                    </dd>
                     <dt>
                        <span>Total # of partitions</span>
                    </dt>
                    <dd>
                        <span>{{getPartitionsCount()}}</span>
                    </dd>

                    <dt>
                        <span>Topics</span>
                    </dt>
                    <div class="table-responsive" v-if="cluster.id">

                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th><span>Topic name</span></th>
                                    <th><span># of partitions</span></th>
                                    <th><span>Is internal?</span></th>
                                </tr>
                            </thead>
                            <tbody>

                                <tr v-for="topic in cluster.topics" :key="topic.name">
                                    <!-- TODO create hyperlink on the topicName to go to the topic details page -->
                                    <td><span>{{topic.name}}</span></td>
                                    <td><span>{{topic.partitions}}</span></td>
                                    <td><span>{{isInternal(topic)}}</span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    
                    <dt>
                        <span>Brokers</span>
                    </dt>
                    <div class="table-responsive" v-if="cluster.id">

                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th><span>Broker ID</span></th>
                                    <th><span>Host</span></th>
                                    <th><span>Port</span></th>
                                    <th><span>Is controller?</span></th>
                                </tr>
                            </thead>
                            <tbody>

                                <tr v-for="broker in cluster.brokers" :key="broker.host">
                                    <!-- TODO create hyperlink on the brokerId to the broker details page -->
                                    <td><span>{{broker.brokerId}}</span></td>
                                    <td><span>{{broker.host}}</span></td>
                                    <td><span>{{broker.port}}</span></td>
                                    <td><span>{{isController(broker)}}</span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="cluster.id" :to="{name: 'ClusterEdit', params: {clusterId: cluster.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./cluster-details.component.ts">
</script>
