<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="topic">
                <h2 class="jh-entity-heading"><span>Topic</span> {{topic.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Name</span>
                    </dt>
                    <dd>
                        <span>{{topic.name}}</span>
                    </dd>
                    <dt>
                        <span>Is Internal</span>
                    </dt>
                    <dd>
                        <span>{{topic.isInternal}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="topic.id" :to="{name: 'TopicEdit', params: {topicId: topic.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./topic-details.component.ts">
</script>
