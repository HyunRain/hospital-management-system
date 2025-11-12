import './assets/main.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import onClickOutside from './util/functions/onClickOutside';

import App from './App.vue';
import router from './router';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.directive('click-outside', onClickOutside);



app.mount('#app');
