<template>
    <div>
        <h2 id="page-heading">
            <span id="cluster-heading">Connected Clusters <b-badge pill variant="info"><span style="font-size: 22px">{{clusters.length}}  </span></b-badge></span>
            <router-link :to="{name: 'ClusterCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-cluster">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span >
                    Connect to a new Cluster
                </span>
            </router-link>
        </h2>
        <b-alert :show="dismissCountDown"
            dismissible
            :variant="alertType"
            @dismissed="dismissCountDown=0"
            @dismiss-count-down="countDownChanged">
            {{alertMessage}}
        </b-alert>
        <br/>
        <div class="alert alert-warning" v-if="!isFetching && clusters && clusters.length === 0">
            <span>No clusters found</span>
        </div>
        <div class="table-responsive" v-if="clusters && clusters.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span>Cluster Id</span></th>
                    <th><span>Name</span></th>
                    <th><span># of brokers</span></th>
                    <th><span># of topics</span></th>
                    <th><span># of partitions</span></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="cluster in clusters" :key="cluster.id">
                    <td>
                        <router-link :to="{name: 'ClusterView', params: {clusterId: cluster.id}}">{{cluster.clusterId}}</router-link>
                    </td>
                    <td>{{cluster.name}}</td>
                    <td>{{getBrokersCount(cluster)}}</td>
                    <td>{{cluster.topicsCount}}</td>
                    <td>{{cluster.partitionsCount}}</td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'ClusterView', params: {clusterId: cluster.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline">View</span>
                            </router-link>
                            <router-link :to="{name: 'ClusterEdit', params: {clusterId: cluster.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(cluster)"
                                   variant="danger"
                                   class="btn btn-sm"
                                   v-b-modal.removeEntity>
                                <font-awesome-icon icon="times"></font-awesome-icon>
                                <span class="d-none d-md-inline">Disconnect</span>
                            </b-button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="kafminApp.cluster.delete.question">Confirm disconnect operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-cluster-heading">Are you sure you want to disconnect from this cluster?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-cluster" v-on:click="removeCluster()">Disconnect</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./cluster.component.ts">
</script>
