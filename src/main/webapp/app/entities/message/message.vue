<template>
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 id="page-heading">
                <span id="message-heading">Messages</span>

                


                <router-link :to="{name: 'MessageCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-message">
                    <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span >
                        Produce a new Message
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
            <div class="alert alert-warning" v-if="!isFetching && messageList.messages && messageList.messages.length === 0">
                <span>No messages found</span>
            </div>

                <div v-if="messageList.messages && messageList.messages.length > 0" >
                    <b-card v-for="message in messageList.messages" :key="message.date" no-body header-tag="header" footer-tag="footer" style="margin-bottom: 20px">
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
