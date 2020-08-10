<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="broker">
                <h2 class="jh-entity-heading"><span>Broker</span> {{broker.brokerId}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Host</span>
                    </dt>
                    <dd>
                        <span>{{broker.host}}</span>
                    </dd>
                    <dt>
                        <span>Port</span>
                    </dt>
                    <dd>
                        <span>{{broker.port}}</span>
                    </dd>
                    <dt>
                        <span>Is Controller</span>
                    </dt>
                    <dd>
                        <span>{{broker.isController}}</span>
                    </dd>
                    <dt>
                        <span>Cluster</span>
                    </dt>
                    <dd>
                        <div v-if="broker.cluster">
                            <router-link :to="{name: 'ClusterView', params: {clusterId: broker.cluster.id}}">{{broker.cluster.name}} ({{broker.cluster.clusterId}})</router-link>
                        </div>
                    </dd>
                </dl>

                <h4><span>Broker Configurations</span></h4>
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
                            <tr v-for="config in broker.configs" :key="config.name">
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
                <router-link v-if="broker.id" :to="{name: 'BrokerEdit', params: {clusterId: broker.cluster.id, brokerId: broker.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./broker-details.component.ts">
</script>
