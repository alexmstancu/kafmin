<template>
    <div class="home row">
        <div class="col-md-3">
            <div class=" img-fluid rounded">
                <img src="content/images/kafka.png" alt="">
            </div>
        </div>
        <div class="col-md-9" style="margin-top: 110px;">
            <h1 class="display-4">Welcome, Kafka Administrator!</h1>
            <!-- <p class="lead">This is your homepage</p> -->

            <div>
                <div class="alert alert-success" v-if="authenticated">
                    <span v-if="username">You are logged in as user "{{username}}"</span>
                </div>

                <div class="alert alert-warning" v-if="!authenticated">
                    <span>If you want to </span>
                    <a class="alert-link" v-on:click="openLogin()">sign in</a><span >, you can try the default accounts:<br/>- Administrator (login="admin" and password="admin") <br/>- User (login="user" and password="user").</span>
                </div>
                <div class="alert alert-warning" v-if="!authenticated">
                    <span>You don't have an account yet?</span>&nbsp;
                    <router-link class="alert-link" to="/register">Register a new account</router-link>
                </div>
            </div>

            <p>
                <span>If you like Kafmin, don't forget to give us a star on</span> <a href="https://github.com/alexmstancu/kafmin" target="_blank" rel="noopener">GitHub</a>!
            </p>
        </div>
    </div>
</template>

<script lang="ts" src="./home.component.ts">
</script>
