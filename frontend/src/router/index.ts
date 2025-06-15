import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/dashboard',
      name: 'AdminLayout',
      redirect: '/login',
      component: () => import('@/layouts/AdminLayout.vue'),
      children: [
        { path: '', name: 'home', component: () => import('@/views/HomeView.vue') },
        {
          path: 'patients',
          name: '/patients',
          component: () => import('@/views/PatientsView.vue'),
        },
      ],
    },
  ],
})

export default router
