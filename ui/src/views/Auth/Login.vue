<template>
    <div class="login-body d-flex justify-content-center align-items-center">
        <div class="bg-light px-5 py-5 text-primary shadow d-flex justify-content-center align-items-center flex-column">
            <b-form-input
                    id="emailInput"
                    v-model="credential.email"
                    type="email"
                    placeholder="Email"
                    style="border-radius: 0;"
                    class="my-2">
            </b-form-input>
            <b-form-input id="passwordInput" v-model="credential.password"
                          type="password"
                          placeholder="Password"
                          style="border-radius: 0;"
                          class="my-2">
            </b-form-input>
            <b-button id="loginButton" v-on:click="login" variant="outline-primary" block squared class="mt-3 mb-2">Zaloguj</b-button>
            <div class="text-center">
                <label class="text-danger" v-if="loginStatus === false"> Błąd logowania</label>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Login',
        data() {
            return {
                credential: {
                    email: "",
                    password: ""
                },
                loginStatus: null
            }
        },
        methods: {
            login() {
                this.$http.post(this.$serverUrl + '/login', this.credential)
                    .then((result) => {
                        if (result.status === 200) {
                            this.$store.dispatch('login', {token: result.body.token, user: result.body.user});
                        }
                    }).catch(err => {
                        this.loginStatus = false;
                    });
            },
            register() {
                this.$router.push({ name: 'Register' });
            }
        }
    }
</script>
