<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="kafminApp.broker.home.createOrEditLabel">Create or edit a Broker</h2>
                <div>
                    <div class="form-group" v-if="broker.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="broker.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="broker-brokerId">Broker Id</label>
                        <input type="text" class="form-control" name="brokerId" id="broker-brokerId"
                            :class="{'valid': !$v.broker.brokerId.$invalid, 'invalid': $v.broker.brokerId.$invalid }" v-model="$v.broker.brokerId.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="broker-host">Host</label>
                        <input type="text" class="form-control" name="host" id="broker-host"
                            :class="{'valid': !$v.broker.host.$invalid, 'invalid': $v.broker.host.$invalid }" v-model="$v.broker.host.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="broker-port">Port</label>
                        <input type="number" class="form-control" name="port" id="broker-port"
                            :class="{'valid': !$v.broker.port.$invalid, 'invalid': $v.broker.port.$invalid }" v-model.number="$v.broker.port.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="broker-isController">Is Controller</label>
                        <input type="checkbox" class="form-check" name="isController" id="broker-isController"
                            :class="{'valid': !$v.broker.isController.$invalid, 'invalid': $v.broker.isController.$invalid }" v-model="$v.broker.isController.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="broker-cluster">Cluster</label>
                        <select class="form-control" id="broker-cluster" name="cluster" v-model="broker.cluster">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="broker.cluster && clusterOption.id === broker.cluster.id ? broker.cluster : clusterOption" v-for="clusterOption in clusters" :key="clusterOption.id">{{clusterOption.name}}</option>
                        </select>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.broker.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./broker-update.component.ts">
</script>
