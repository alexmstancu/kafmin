<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
                <h2 id="kafminApp.topic.home.createOrEditLabel">Create or edit a Topic</h2>
                <div>

                    <div class="form-group">
                        <label class="form-control-label" for="topic-name">Name</label>
                        <input type="text" class="form-control" name="name" id="topic-name"
                            :class="{'valid': !$v.topic.name.$invalid, 'invalid': $v.topic.name.$invalid }" v-model="$v.topic.name.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="topic-numPartitions">Number of partitions</label>
                        <input type="text" class="form-control" name="numPartitions" id="topic-numPartitions" 
                            :class="{'valid': !$v.topic.numPartitions.$invalid, 'invalid': $v.topic.numPartitions.$invalid }" v-model="$v.topic.numPartitions.$model" />
                    </div>
                    
                    <div class="form-group">
                        <label class="form-control-label" for="topic-repl">Replication factor</label>
                        <input type="text" class="form-control" name="repl" id="topic-repl"
                            :class="{'valid': !$v.topic.replicationFactor.$invalid, 'invalid': $v.replicationFactor.name.$invalid }" v-model="$v.topic.replicationFactor.$model" />
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
