<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="kafminApp.cluster.home.createOrEditLabel">Create or edit a Cluster</h2>
                <div>
                    <div class="form-group" v-if="cluster.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="cluster.id" readonly />
                    </div>
                    <div class="form-group" v-if="cluster.id">
                        <label class="form-control-label" for="cluster-clusterId">Cluster Id</label>
                        <input type="text" class="form-control" id="id" name="id" v-model="cluster.clusterId" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="cluster-name">Cluster Name</label>
                        <input type="text" class="form-control" name="name" id="cluster-name"
                            :class="{'valid': !$v.cluster.name.$invalid, 'invalid': $v.cluster.name.$invalid }" v-model="$v.cluster.name.$model" />
                    </div>

                    <!-- list the brokers and let the user add more brokers (host & port only) ONLY IN CASE OF CREATE -->
                    <label v-if="!cluster.id" class="form-control-label" for="brokers-table">Add the initial bootstrap server</label>
                    <!-- <div class="table-responsive" v-if="brokers && brokers.length > 0"> -->

                    <div class="table-responsive" v-if="!cluster.id">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th><span>Host</span></th>
                                    <th><span>Port</span></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="text" class="form-control" id="id" name="id" v-model="initialBroker.host"/></td>
                                    <td><input type="text" class="form-control" id="id" name="id" v-model="initialBroker.port"/></td>
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
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./cluster-update.component.ts">
</script>
