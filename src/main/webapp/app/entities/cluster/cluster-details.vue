<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="cluster">
                <h2 class="jh-entity-heading">
                    <span>Cluster details</span> 

                    <router-link v-if="cluster.id" :to="{name: 'ClusterEdit', params: {clusterId: cluster.id}}" tag="button" class="btn btn-primary float-right">
                        <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit cluster name</span>
                    </router-link>

                    <button type="submit"
                            v-on:click.prevent="previousState()"
                            class="btn btn-info float-right">
                        <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                    </button>
                </h2>

                <dl class="row jh-entity-details">
                    <dt>
                        <span>Name</span>
                    </dt>
                    <dd>
                        <span>{{cluster.name}}</span>
                    </dd>
                    <dt>
                        <span>Cluster Id</span>
                    </dt>
                    <dd>
                        <span>{{cluster.clusterId}}</span>
                    </dd>
                    <dt>
                        <span>Total number of topics</span>
                    </dt>
                    <dd>
                        <span>{{getTopicsCount()}}</span>
                    </dd>
                    <dt>
                        <span>Total number of partitions</span>
                    </dt>
                    <dd>
                        <span>{{getPartitionsCount()}}</span>
                    </dd>
                </dl>

                <h4>
                    <span>Topics</span>
                    <router-link :to="{name: 'TopicCreate', params: {clusterDbId: cluster.id, clusterName: cluster.name, clusterInternalId: cluster.clusterId}}" tag="button" id="jh-create-entity" class="btn btn-primary btn-sm float-right jh-create-entity create-topic">
                        <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span>Create new Topic</span>
                    </router-link>
                </h4>

                <div class="alert alert-warning" v-if="cluster.id && (!cluster.topics ||  cluster.topics.length === 0)">
                    <span>No topics found</span>
                </div>


                <!-- <div class="table-responsive" v-if="cluster.id && !isFetchingTopics"> -->
                <div class="table-responsive" v-if="cluster.id && cluster.topics && cluster.topics.length > 0">
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
                            <tr v-for="topic in sortedTopics" :key="topic.name">
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
                                        <router-link :to="{name: 'Message', params: {clusterDbId: cluster.id, topicName: topic.name}}" tag="button" class="btn btn-outline-secondary btn-sm details">
                                            <font-awesome-icon icon="envelope-open-text"></font-awesome-icon>
                                            <span class="d-none d-md-inline">Consume</span>
                                        </router-link>
                                        <router-link :to="{name: 'MessageCreate', params: {clusterDbId: cluster.id, topicName: topic.name, clusterName: cluster.name, clusterInternalId: cluster.clusterId}}" 
                                                    tag="button" class="btn btn-outline-success btn-sm details" :disabled="topic.internal">
                                            <font-awesome-icon icon="paper-plane"></font-awesome-icon>
                                            <span class="d-none d-md-inline">Produce</span>
                                        </router-link>
                                        <router-link :to="{name: 'TopicView', params: {clusterDbId: cluster.id, topicName: topic.name}}" tag="button" class="btn btn-info btn-sm details">
                                            <font-awesome-icon icon="eye"></font-awesome-icon>
                                            <span class="d-none d-md-inline">View</span>
                                        </router-link>
                                        <router-link :to="{name: 'TopicEdit', params: {clusterDbId: cluster.id, topicName: topic.name}}"  tag="button" class="btn btn-primary btn-sm edit">
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
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit cluster name</span>
                </router-link>
            </div>
        </div>


    <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="kafminApp.cluster.delete.topic.question">Confirm topic deletion</span></span>
            <div class="modal-body">
                <p id="jhi-delete-cluster-heading">Are you sure you want to delete this topic?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-cluster" v-on:click="removeTopic()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./cluster-details.component.ts">
</script>
