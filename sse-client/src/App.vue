<template>
  <form v-if="!userId" @submit.prevent="submit">
    <label for="input-user-id">유저ID</label>
    <input type="text" inputmode="numeric" v-model="inputUserId" placeholder="유저ID를 입력해주세요. (1 또는 2만 입력)" id="input-user-id">
    <button type="submit">완료</button>
  </form>

  <div v-else>
    <h1>게시글 목록</h1>
    <ul>
      <li v-for="post in posts" :key="post.postId">
        {{ post.title }}
        <button type="button" @click="clickLike(post.postId)">좋아요</button>
        (작성자: 유저 {{ post.userId }}번)
      </li>
    </ul>

    <h4>현재 접속된 유저 ID: {{ userId }}</h4>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';

const inputUserId = ref("");
const userId = ref(0);
const posts = ref([]);

let eventSource = null;

const fetchPosts = async () => {
  const response = await fetch("http://localhost:7777/posts");
  const data = await response.json();
  posts.value = data;
}

// SSE 구독 처리
const subscribeServerEvents = () => {
  eventSource = new EventSource(`http://localhost:7777/notification/subscribe/${userId.value}`);

  eventSource.onopen = async () => {
    await console.log("sse opened!")
  }

  eventSource.addEventListener('like', (event) => {
    const { data } = event;
    window.alert(data);
  });

  eventSource.onerror = async (e) => {
    await console.log(e)
  }
}

onMounted(() => {
  fetchPosts();
});

watch(userId, () => {
  if (!userId.value) return;

  subscribeServerEvents();
})

onUnmounted(() => {
  if (eventSource) {
    eventSource.close();
  }
})

const submit = () => {
  if (!inputUserId.value) {
    window.alert("유저번호를 입력해주세요!");
    return;
  }

  userId.value = Number(inputUserId.value);
}

const clickLike = async (clickedPostId) => {
  await fetch(`http://localhost:7777/posts/${clickedPostId}/like`, {
    method: "PUT"
  });
}
</script>

<style scoped>
input {
  width: 300px;
}
</style>