<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
                <h2 id="kafminApp.topic.home.createOrEditLabel">Create or edit a Topic</h2>
                <div>
                    <div class="form-group" v-if="topic.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="topic.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="topic-name">Name</label>
                        <input type="text" class="form-control" name="name" id="topic-name"
                            :class="{'valid': !$v.topic.name.$invalid, 'invalid': $v.topic.name.$invalid }" v-model="$v.topic.name.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="topic-isInternal">Is Internal</label>
                        <input type="checkbox" class="form-check" name="isInternal" id="topic-isInternal"
                            :class="{'valid': !$v.topic.isInternal.$invalid, 'invalid': $v.topic.isInternal.$invalid }" v-model="$v.topic.isInternal.$model" />
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.topic.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./topic-update.component.ts">
</script>
