<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="kafminApp.message.home.createOrEditLabel">Create or edit a Message</h2>
                <div>
                    <div class="form-group">
                        <label class="form-control-label" for="message-key">Key</label>
                        <input type="text" class="form-control" name="key" id="message-key"
                            :class="{'valid': !$v.message.key.$invalid, 'invalid': $v.message.key.$invalid }" v-model="$v.message.key.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="message-message">Message</label>

                          <b-form-textarea id="textarea-rows"  placeholder="Message" rows="8" v-model="message.message">

                          </b-form-textarea>

                        <!-- <input type="text" class="form-control" name="message" id="message-message"
                            :class="{'valid': !$v.message.message.$invalid, 'invalid': $v.message.message.$invalid }" v-model="$v.message.message.$model" /> -->
                    </div>


                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.message.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./message-update.component.ts">
</script>
