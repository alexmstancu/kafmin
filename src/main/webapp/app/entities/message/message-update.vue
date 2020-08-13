<template>
    <div class="row justify-content-center">
        <div class="col-8">

            <h2 id="kafminApp.message.home.createOrEditLabel">Produce a new Message</h2>
            <dl class="row jh-entity-details">
                <dt>
                    <span>Topic name</span>
                </dt>
                <dd>
                    <router-link :to="{name: 'TopicView', params: {clusterDbId: clusterDbId, topicName: topicName}}">
                       {{topicName}} 
                    </router-link>
                </dd>
                <dt>
                    <span>Cluster</span>
                </dt>
                <dd>
                    <router-link :to="{name: 'ClusterView', params: {clusterId: clusterDbId}}">
                        {{clusterName}} ({{clusterInternalId}})
                        </router-link>
                </dd>
            </dl>
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <div>
                    <div class="form-group">
                        <label class="form-control-label" for="message-key">Key</label>
                        <input type="text" class="form-control" name="key" id="message-key"
                            :class="{'valid': !$v.message.key.$invalid, 'invalid': $v.message.key.$invalid }" v-model="$v.message.key.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="message-message">Message</label>

                          <b-form-textarea id="textarea-rows" rows="8" v-model="message.message">

                          </b-form-textarea>

                        <!-- <input type="text" class="form-control" name="message" id="message-message"
                            :class="{'valid': !$v.message.message.$invalid, 'invalid': $v.message.message.$invalid }" v-model="$v.message.message.$model" /> -->
                    </div>


                </div>
                <div class="btn-group float-left">
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.message.$invalid || isSaving" class="btn btn-outline-success">
                        <font-awesome-icon icon="paper-plane"></font-awesome-icon>&nbsp;<span>Produce</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./message-update.component.ts">
</script>
