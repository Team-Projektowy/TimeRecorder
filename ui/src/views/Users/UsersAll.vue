<template>
  <div class="container text-center">
    <h2 class="mb-4">Konta użytkowników</h2>
    <b-list-group class="mb-4">
      <b-list-group-item v-for="user in users" :key="user.id" class="d-flex justify-content-between align-items-center">
        <span>({{ user.id }}) {{ user.firstName }} {{ user.lastName}}, {{ user.position}}, {{ user.hoursAWeek }} godzin tygodniowo</span>
        <div>
          <b-button variant="info" size="sm" squared class="mr-3" :to="`/users/${user.id}/edit`">Edytuj</b-button>
          <b-button variant="danger" size="sm" squared @click="deleteUser(user.id)">Usuń</b-button>
        </div>
      </b-list-group-item>
    </b-list-group>
    <b-button variant="primary" squared to="/users/create">Dodaj użytkownika</b-button>
  </div>
</template>

<script>
export default {
  name: "UsersAll",
  data() {
    return {
      users: [],
    }
  },
  beforeMount() {
    this.$http.get(this.$serverUrl + "/users")
        .then(res => {
          this.users = res.body;
        });
  },
  methods: {
    deleteUser(userId) {
      this.$http.delete(this.$serverUrl + "/users/" + userId)
          .then(() => {
            this.users = this.users.filter(user => user.id !== userId);
          })
    }
  }
}
</script>

<style scoped>

</style>
