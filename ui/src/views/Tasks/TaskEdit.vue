<template>
  <div class="login-body d-flex justify-content-center align-items-center" style="min-width: 600px">
    <div class="bg-light shadow px-5 py-5 text-primary d-flex justify-content-center align-items-center flex-column">
      <div class="form-group">
        <label for="name">Nazwa</label>
        <input v-model.trim="$v.task.name.$model" :class="{ 'border-danger' : $v.task.name.$error }" id="name" class="form-control" type="text">
        <div v-if="$v.task.name.$error">
          <div class="text-danger" v-if="!$v.task.name.required">Nazwa jest wymagana.</div>
          <div class="text-danger" v-if="!$v.task.name.minLength">Nazwa musi mieć co najmniej 1 znak</div>
          <div class="text-danger" v-if="!$v.task.name.maxLength">Nazwa może zawierać co najwyżej 255 znaków.</div>
        </div>
        <div class="text-left mt-3">
          <label for="description">Opis</label>
          <textarea v-model.trim="$v.task.description.$model" :class="{ 'border-danger' : $v.task.description.$error }" id="description" class="form-control"></textarea>
          <div v-if="$v.task.description.$error">
            <div class="text-danger" v-if="!$v.task.description.maxLength">Nazwa może zawierać co najwyżej 1024 znaki.</div>
          </div>
        </div>
      </div>
      <div class="mt-3 text-center">
        <b-button @click="editTask" variant="outline-primary" block squared>Edytuj zadanie</b-button>
        <p class="mt-3 text-danger" v-if="submitStatus === 'ERROR'">Proszę wypełnić poprawnie.</p>
      </div>
    </div>
  </div>
</template>

<script>


import {maxLength, minLength, required} from "vuelidate/lib/validators";

export default {
  name: "EditTask",
  data() {
    return {
      taskId: null,
      task: {
        name: null,
        description: null,
      },
      submitStatus: null,
    }
  },
  created() {
    this.taskId = this.$route.params.taskId;
    this.$http.get(this.$serverUrl + "/tasks/" + this.taskId)
        .then(res => {
          this.task.name = res.body.name;
          this.task.description = res.body.description;
        })
  },
  methods: {
    editTask() {
      if (this.$v.$invalid) {
        this.submitStatus = 'ERROR'
      }
      else {
        this.$http.put(this.$serverUrl + "/tasks/" + this.taskId, this.task)
            .then(() => {
              this.$router.push("/tasks");
            });
      }
    }
  },
  validations: {
    task: {
      name: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(255),
      },
      description: {
        maxLength: maxLength(1024),
      },
    }
  }
}
</script>

<style scoped>

</style>
