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
