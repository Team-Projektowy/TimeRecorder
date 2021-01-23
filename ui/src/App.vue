<template>
  <div id="app">
    <Navbar/>
    <router-view class="pt-5" />
  </div>
</template>

<script>
import Navbar from "./components/Navbar";

export default {
  name: 'App',
  components: {
    Navbar,
  },
}
</script>
