<template>
  <div class="container text-center">
    <h2 class="mb-4">Kategorie zadań</h2>
    <b-list-group class="mb-4">
      <b-list-group-item v-for="task in tasks" :key="task.id" class="d-flex justify-content-between align-items-center">
        <span v-b-tooltip :title="task.description">{{ task.name }}</span>
        <b-button variant="info" size="sm" squared class="mr-3" :to="`/tasks/${task.id}/edit`">Edytuj</b-button>
      </b-list-group-item>
    </b-list-group>
    <b-button variant="primary" squared to="/tasks/create">Dodaj zadanie</b-button>
  </div>
</template>

<script>
export default {
  name: "TasksAll",
  data() {
    return {
      tasks: [],
    }
  },
  beforeMount() {
    this.$http.get(this.$serverUrl + "/tasks")
        .then(res => {
          this.tasks = res.body;
          console.log(this.tasks);
        });
  },
}
</script>

<style scoped>

</style>

