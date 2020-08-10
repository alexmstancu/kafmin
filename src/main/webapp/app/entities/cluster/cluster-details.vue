<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="cluster">
                <h2 class="jh-entity-heading"><span>Cluster</span> '{{cluster.name}}'</h2>
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
                </dl>
                <!-- TODO ADD BUTTON TO CREATE TOPIC -->

                <h4><span>Topics</span></h4>

                <div class="table-responsive" v-if="cluster.id">
                    <table class="table table-sm table-striped table-bordered">
                        <thead>
                            <tr>
                                <th><span>Topic name</span></th>
                                <th><span># of partitions</span></th>
                                <th><span>Is internal?</span></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>

                            <tr v-for="topic in cluster.topics" :key="topic.name">
                                <td>
                                    <span>
                                        <router-link :to="{name: 'TopicView', params: {clusterDbId: cluster.id, topicName: topic.name}}">
                                            {{topic.name}}
                                        </router-link>

                                    </span>
                                </td>
                                <td><span>{{topic.partitions}}</span></td>
                                <td><span>{{isInternal(topic)}}</span></td>
                                <td class="text-right">
                                    <div class="btn-group">
                                        <router-link :to="{name: 'TopicView', params: {clusterDbId: cluster.id, topicName: topic.name}}" tag="button" class="btn btn-info btn-sm details">
                                            <font-awesome-icon icon="eye"></font-awesome-icon>
                                            <span class="d-none d-md-inline">View</span>
                                        </router-link>
                                        <router-link :to="{name: 'TopicEdit', params: {topicId: topic.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                            <span class="d-none d-md-inline">Edit</span>
                                        </router-link>
                                        <b-button v-on:click="prepareRemove(topic)"
                                            variant="danger"
                                            class="btn btn-sm"
                                            v-b-modal.removeEntity>
                                            <font-awesome-icon icon="times"></font-awesome-icon>
                                            <span class="d-none d-md-inline">Delete</span>
                                        </b-button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <h4><span>Brokers</span></h4>
                <div class="table-responsive" v-if="cluster.id">
                    <table class="table table-sm table-striped table-bordered">
                        <thead>
                            <tr>
                                <th><span>Broker ID</span></th>
                                <th><span>Host</span></th>
                                <th><span>Port</span></th>
                                <th><span>Is controller?</span></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="broker in cluster.brokers" :key="broker.host">
                                <td>
                                    <span>
                                        <router-link :to="{name: 'BrokerView', params: {clusterId: cluster.id, brokerId: broker.brokerId}}">
                                            broker {{broker.brokerId}}
                                        </router-link>
                                    </span>
                                </td>
                                <td><span>{{broker.host}}</span></td>
                                <td><span>{{broker.port}}</span></td>
                                <td><span>{{isController(broker)}}</span></td>
                                <td class="text-left">
                                    <div class="btn-group">
                                        <router-link :to="{name: 'BrokerView', params: {clusterId: cluster.id, brokerId: broker.brokerId}}" tag="button" class="btn btn-info btn-sm details">
                                            <font-awesome-icon icon="eye"></font-awesome-icon>
                                            <span class="d-none d-md-inline">View</span>
                                        </router-link>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

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
