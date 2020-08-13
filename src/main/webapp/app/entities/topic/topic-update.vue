<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <!-- UPDATE EXISTING TOPIC CASE -->
            <div v-if="topic.cluster !== undefined">
                <h2  id="kafminApp.topic.home.createOrEditLabel2">Edit topic configuration</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Name</span>
                    </dt>
                    <dd>
                        <span>{{topic.name}}</span>
                    </dd>
                    <dt>
                        <span>Is Internal?</span>
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
            </div>

            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
                <div>
                    <button type="submit" id="save-entity" :disabled="$v.topic.$invalid || isSaving" class="btn btn-primary float-right">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Create</span>
                    </button>
                    <button type="button" id="cancel-save" class="btn btn-secondary float-right" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>

                </div>

                <!-- CREATE NEW TOPIC CASE -->
                <div v-if="topic.cluster === undefined">
                    <h2 id="kafminApp.topic.home.createOrEditLabel">Create a Topic</h2>

                    <dl class="row jh-entity-details">
                        <dt>
                            <span>Cluster</span>
                        </dt>
                        <dd>
                            <router-link :to="{name: 'ClusterView', params: {clusterId: savedClusterDbId}}"> {{clusterName}} ({{clusterInternalId}})</router-link>
                        </dd>
                    </dl>

                    <div>
                        <div class="form-group">
                            <label class="form-control-label" for="topic-name">Name</label>
                            <input type="text" class="form-control" name="name" id="topic-name"
                                :class="{'valid': !$v.topic.name.$invalid, 'invalid': $v.topic.name.$invalid }" v-model="$v.topic.name.$model" />
                        </div>
                        <div class="form-group">
                            <label class="form-control-label" for="topic-numPartitions">Number of partitions</label>
                            <input class="form-control" name="numPartitions" id="topic-numPartitions" 
                                :class="{'valid': !$v.topic.numPartitions.$invalid, 'invalid': $v.topic.numPartitions.$invalid }" v-model.number="$v.topic.numPartitions.$model" />
                        </div>
                        
                        <div class="form-group">
                            <label class="form-control-label" for="topic-replicationFactor">Replication factor</label>
                            <input  class="form-control" name="replicationFactor" id="topic-replicationFactor"
                                :class="{'valid': !$v.topic.replicationFactor.$invalid, 'invalid': $v.topic.replicationFactor.$invalid }" v-model.number="$v.topic.replicationFactor.$model" />
                        </div>
                    </div>
                </div>  
                <div v-if="topic.cluster !== undefined">
                    <h4><span> Update non-readonly configurations</span></h4>
                    <div class="table-responsive">
                        <table class="table table-sm table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th><span>Name</span></th>
                                    <th><span>Value</span></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="config in getNonReadOnlyConfigs()" :key="config.name">
                                    <td><span>{{config.name}}</span></td>
                                    <td ><span><input v-bind:style="applyStyleIfEdited(config)" type="text" class="form-control" id="cfg" name="cfg" v-model="config.value"/></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </div>


                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.topic.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Create</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./topic-update.component.ts">
</script>
