<template>
  <div class="login-body d-flex justify-content-center align-items-center" style="min-width: 400px">
    <div class="bg-light shadow px-5 py-5 text-primary d-flex justify-content-center align-items-center flex-column">
      <div class="form-group">
        <label for="oldPassword">Stare hasło</label>
        <input v-model.trim="$v.credential.oldPassword.$model" :class="{ 'border-danger': $v.credential.oldPassword.$error }" id="oldPassword" class="form-control" type="password">
        <div v-if="$v.credential.oldPassword.$error">
          <div class="text-danger" v-if="!$v.credential.oldPassword.required">Stare hasło jest wymagane.</div>
        </div>
        <label for="newPassword">Nowe hasło</label>
        <input v-model.trim="$v.credential.newPassword.$model" :class="{ 'border-danger': $v.credential.newPassword.$error }" id="newPassword" class="form-control" type="password">
        <div v-if="$v.credential.newPassword.$error">
          <div class="text-danger" v-if="!$v.credential.newPassword.required">Nowe hasło jest wymagane.</div>
          <div class="text-danger" v-if="!$v.credential.newPassword.minLength">Nowe hasło musi zawierać conajmniej 8 znaków.</div>
          <div class="text-danger" v-if="!$v.credential.newPassword.maxLength">Nowe hasło może zawierać conajwyżej 255 znaków.</div>
        </div>
      </div>
      <div class="mt-3 text-center">
        <b-button @click="changePassword" variant="outline-primary" block squared>Zmień hasło</b-button>
        <p class="mt-3 text-danger" v-if="submitStatus === 'ERROR'">Proszę wypełnić poprawnie.</p>
        <div class="text-center mt-3">
          <label class="text-danger" v-if="requestFailed">Niepoprawne stare hasło</label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {required, maxLength, minLength} from 'vuelidate/lib/validators'
export default {
  name: 'ChangePassword',
  data() {
    return {
      credential: {
        oldPassword: null,
        newPassword: null,
      },
      submitStatus: null,
      requestFailed: false,
    }
  },
  validations: {
    credential: {
      oldPassword: {
        required
      },
      newPassword: {
        required,
        minLength: minLength(8),
        maxLength: maxLength(255),
      }
    }
  },
  methods: {
    changePassword() {
      if (this.$v.$invalid) {
        this.submitStatus = 'ERROR'
      }
      else {
        this.$http.put(this.$serverUrl + '/change-password', this.credential)
            .then(() => {
              alert("Pomyślnie zmieniono hasło!");
              this.$router.push({ name: "Home" });
            })
            .catch(() => {
              this.requestFailed = true;
            })
      }
    },
  }
}
</script>

<style scoped>
input {
  min-width: 350px;
}
</style>
