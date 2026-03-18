<script setup>
const route = useRoute();
const router = useRouter();

function onBack() {
  if (window.history.state.back) history.back();
  else router.replace("/");
}

const title = computed(() => {
  if (!route.meta) return "";

  return route.meta.title || "";
});

const showLeftArrow = computed(() => {
  if (!route.meta) return false;

  return route.meta.level === 2;
});
</script>

<template>
  <VanNavBar
    :title="title"
    :fixed="true"
    clickable
    placeholder
    :left-arrow="showLeftArrow"
    @click-left="onBack"
  />
</template>

<style scoped></style>
