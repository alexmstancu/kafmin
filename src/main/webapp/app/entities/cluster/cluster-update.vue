<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 v-if="cluster.id" id="kafminApp.cluster.home.createOrEditLabel">Edit a Cluster</h2>
                <h2 v-if="!cluster.id" id="kafminApp.cluster.home.createOrEditLabel1">Connect to a new Cluster</h2>
                <div>
                    <!-- <div class="form-group" v-if="cluster.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="cluster.id" readonly />
                    </div> -->

                    <dl class="row jh-entity-details">
                        <dt v-if="cluster.id">
                            <span>Cluster Id</span>
                        </dt>
                        <dd v-if="cluster.id">
                            <span>{{cluster.clusterId}}</span>
                        </dd>
                        <dt v-if="cluster.id">
                            <span>Total # of topics</span>
                        </dt>
                        <dd v-if="cluster.id">
                            <span>{{getTopicsCount()}}</span>
                        </dd>
                        <dt v-if="cluster.id">
                            <span>Total # of partitions</span>
                        </dt>
                        <dd v-if="cluster.id">
                            <span>{{getPartitionsCount()}}</span>
                        </dd>
                    </dl>

                    <b-alert :show="dismissCountDown"
                        dismissible
                        :variant="alertType"
                        @dismissed="dismissCountDown=0"
                        @dismiss-count-down="countDownChanged">
                        {{alertMessage}}
                    </b-alert>

                    <!-- <div class="form-group" v-if="cluster.id">
                        <label class="form-control-label" for="cluster-clusterId">Cluster Id</label>
                        <input type="text" class="form-control" id="id" name="id" v-model="cluster.clusterId" readonly />
                    </div> -->
                    <div class="form-group">
                        <label class="form-control-label" for="cluster-name">New cluster name</label>
                        <input type="text" class="form-control" name="name" id="cluster-name"
                            :class="{'valid': !$v.cluster.name.$invalid, 'invalid': $v.cluster.name.$invalid }" v-model="$v.cluster.name.$model" />
                    </div>

                    <!-- list the brokers and let the user add more brokers (host & port only) ONLY IN CASE OF CREATE -->
                    <label v-if="!cluster.id" class="form-control-label" for="brokers-table">Add the initial bootstrap broker</label>
                    <div class="table-responsive table table-sm table-striped table-bordered" v-if="!cluster.id">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th><span>Host</span></th>
                                    <th><span>Port</span></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="broker in cluster.brokers" :key="broker.brokerId">
                                    <td><input type="text" class="form-control" id="id" name="id" v-model="broker.host"/></td>
                                    <td><input type="text" class="form-control" id="id" name="id" v-model="broker.port"/></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.cluster.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                    <button :disabled="isAddBrokerButtonDisabled()" v-on:click="addBroker()" class="btn btn-outline-info float-right">
                        <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>Add more brokers</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./cluster-update.component.ts">
</script>
