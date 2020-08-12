<template>
    <div class="row justify-content-center">
        <div v-if="!isFetching" class="col-8">
            <h2 id="page-heading">
                <span  id="message-heading">Messages in topic {{messageList.topic}}</span>

                <router-link :to="{name: 'MessageCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-message">
                    <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span >
                        Produce a new Message
                    </span>
                </router-link>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info float-right">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
            </h2>

            <dl class="row jh-entity-details">
                <dt>
                    <span>Topic name</span>
                </dt>
                <dd>
                    <span>{{messageList.topic}}</span>
                </dd>
                <dt>
                    <span>Total number of partitions</span>
                </dt>
                <dd>
                    <span>{{messageList.partitionsCount}}</span>
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
            <br/>
            <div class="alert alert-warning" v-if="!isFetching && messageList.messages && messageList.messages.length === 0">
                <span>No messages found</span>
            </div>

                <div v-if="messageList.messages && messageList.messages.length > 0" >
                    <b-card v-for="message in messagesSortedByOffsetDescending()" :key="message.date" no-body header-tag="header" footer-tag="footer" style="margin-bottom: 20px">
                        <template v-slot:header >
                            <p style="margin: 0px">
                                <b-badge variant="success">Key</b-badge>  <b>{{message.key}}</b>
                                <b-badge variant="info">Offset</b-badge>  <small>{{message.offset}}</small>
                                <b-badge variant="info">Partition</b-badge> <small>{{message.partition}}</small>
                                <b-badge variant="info">Timestamp</b-badge>  <small>{{getPrettyDate(message.date)}}</small> 
                            </p>
                        </template>
                        <b-card-body>
                            <b-card-text>
                                {{message.message}}
                            </b-card-text>
                        </b-card-body>
                    </b-card>
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
