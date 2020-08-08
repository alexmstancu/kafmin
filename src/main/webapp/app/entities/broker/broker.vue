<template>
    <div>
        <h2 id="page-heading">
            <span id="broker-heading">Brokers</span>
            <router-link :to="{name: 'BrokerCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-broker">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span >
                    Create a new Broker
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
        <div class="alert alert-warning" v-if="!isFetching && brokers && brokers.length === 0">
            <span>No brokers found</span>
        </div>
        <div class="table-responsive" v-if="brokers && brokers.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <!-- <th><span>ID</span></th> -->
                    <th><span>Broker Id</span></th>
                    <th><span>Host</span></th>
                    <th><span>Port</span></th>
                    <th><span>Is Controller</span></th>
                    <th><span>Cluster</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="broker in brokers"
                    :key="broker.id">
                    <td>
                        <!-- TODO move the router link to brokerId -->
                        <router-link :to="{name: 'BrokerView', params: {brokerId: broker.id}}">{{broker.id}}</router-link>
                    </td>
                    <td>{{broker.brokerId}}</td>
                    <td>{{broker.host}}</td>
                    <td>{{broker.port}}</td>
                    <td>{{broker.isController}}</td>
                    <td>
                        <div v-if="broker.cluster">
                            <router-link :to="{name: 'ClusterView', params: {clusterId: broker.cluster.id}}">{{broker.cluster.name}}</router-link>
                        </div>
                    </td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'BrokerView', params: {brokerId: broker.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline">View</span>
                            </router-link>
                            <router-link :to="{name: 'BrokerEdit', params: {brokerId: broker.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(broker)"
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
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="kafminApp.broker.delete.question">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-broker-heading">Are you sure you want to delete this Broker?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-broker" v-on:click="removeBroker()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./broker.component.ts">
</script>
