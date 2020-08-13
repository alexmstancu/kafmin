<template>
    <div class="row justify-content-center">
        <div v-if="!isFetching && messageList" class="col-8">
            <h2 id="page-heading">
                <span  id="message-heading">Messages in topic {{messageList.topic}}</span>
                <div class="btn-group float-right">
                    <button type="submit"
                            v-on:click.prevent="previousState()"
                            class="btn btn-info">
                        <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                    </button>
                    <router-link 
                        :to="{name: 'MessageCreate', params: {clusterDbId: messageList.cluster.id, topicName: messageList.topic, clusterName: messageList.cluster.name, clusterInternalId: messageList.cluster.clusterId}}" 
                        tag="button" id="jh-create-entity" class="btn btn-outline-success">
                        <font-awesome-icon icon="paper-plane"></font-awesome-icon>
                            Produce a new Message
                    </router-link>

                </div>
            </h2>

            <dl class="row jh-entity-details">
                <dt>
                    <span>Topic name</span>
                </dt>
                <dd>
                    <router-link :to="{name: 'TopicView', params: {clusterDbId: messageList.cluster.id, topicName: messageList.topic}}">
                        {{messageList.topic}}
                    </router-link>
                </dd>
                <dt>
                    <span>Number of partitions</span>
                </dt>
                <dd>
                    <span>{{messageList.partitionsCount}}</span>
                </dd>
                <dt>
                    <span>Total number of messages</span>
                </dt>
                <dd>
                    <span>{{messageList.messages.length}}</span>
                </dd>
                <dt>
                    <span>Cluster</span>
                </dt>
                <dd>
                    <router-link :to="{name: 'ClusterView', params: {clusterId: messageList.cluster.id}}">{{messageList.cluster.name}} ({{messageList.cluster.clusterId}})</router-link>
                </dd>
            </dl>

            <b-alert :show="dismissCountDown"
                dismissible
                :variant="alertType"
                @dismissed="dismissCountDown=0"
                @dismiss-count-down="countDownChanged">
                {{alertMessage}}
            </b-alert>
            <div>
                <b-dropdown id="dropdown-1" :text="getFilterText()" class="m-md-2">
                    <b-dropdown-item @click="resetPartitionFilter()">All</b-dropdown-item>
                    <b-dropdown-item v-for="p in partitionsArray" :key="p" @click="setPartitionFilter(p)">
                        Partition <b>{{p}}</b> 
                    </b-dropdown-item>
                </b-dropdown>

                <span>
                    <b-badge pill variant="warning">Number of messages: <b-badge pill> <span style="font-size: 12px">{{filteredAndSortedMessages.length}}</span> </b-badge> </b-badge>
                </span>
            </div>
            
            <div class="alert alert-warning" v-if="!isFetching && filteredAndSortedMessages && filteredAndSortedMessages.length === 0">
                <span>No messages found</span>
            </div>

            <div v-if="!isFetching && filteredAndSortedMessages && filteredAndSortedMessages.length > 0" >
                <b-card v-for="message in filteredAndSortedMessages" :key="message.date" no-body header-tag="header" footer-tag="footer" style="margin-bottom: 20px">
                    <template v-slot:header >
                        <p style="margin: 0px">
                            <b-badge variant="info">Offset</b-badge>    {{message.offset}}
                            <b-badge variant="info">Partition</b-badge> {{message.partition}}
                            <b-badge variant="info">Timestamp</b-badge> <small>{{getPrettyDate(message.date)}}</small> 
                            <b-badge pill variant="success">Key</b-badge> <b>{{message.key}}</b>
                        </p>
                    </template>
                    <b-card-body>
                        <b-card-text>
                            {{message.message}}
                        </b-card-text>
                    </b-card-body>
                </b-card>
            </div>

            <div class="btn-group float-left">

                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link :to="{name: 'MessageCreate'}" tag="button" id="jh-create-entity" class="btn  btn-outline-success">
                    <font-awesome-icon icon="paper-plane"></font-awesome-icon>
                    <span >
                        Produce a new Message
                    </span>
                </router-link>
            </div>
            
            <b-modal ref="removeEntity" id="removeEntity" >
                <span slot="modal-title"><span id="kafminApp.message.delete.question">Confirm delete operation</span></span>
                <div class="modal-body">
                    <p id="jhi-delete-message-heading">Are you sure you want to delete this Message?</p>
                </div>
                <div slot="modal-footer">
                    <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                    <button type="button" class="btn btn-primary" id="jhi-confirm-delete-message" v-on:click="removeMessage()">Delete</button>
                </div>
            </b-modal>
        </div>
    </div>
</template>

<script lang="ts" src="./message.component.ts">
</script>
