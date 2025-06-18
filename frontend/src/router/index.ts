import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { useToggleStore } from '@/stores/toggleStore';


async function isAuthenticated() {
  const authStore = useAuthStore();
  const toggleStore = useToggleStore();

  if(toggleStore.isDemo) return;

  if (!authStore.isInitialised) {
    try {
      await authStore.reauthenticate()
      if (authStore.role && authStore.email) {
        await authStore.fetchStaffData(authStore.role, authStore.email);
      }
    } catch (error) {
      console.error(error);
    }
  } else {
    return;
  }
  if (!authStore.isLoggedIn) {
    return { path: '/login' };
  }
};

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
      component: () => import('@/layouts/AdminLayout.vue'),
      children: [
        { path: '', name: 'home', component: () => import('@/views/HomeView.vue') },
        { path: 'doctors', name: 'doctors', component: () => import('@/views/DoctorView.vue') },
        { path: 'patients', name: 'patients', component: () => import('@/views/PatientsView.vue') },
        { path: 'appointments', name: 'appointments', component: () => import('@/views/AppointmentView.vue') },
        { path: 'bed-manager', name: 'bed-manager', component: () => import('@/views/BedManagerView.vue') },
        { path: 'departments', name: 'departments', component: () => import('@/views/DepartmentView.vue') },
        { path: 'employees', name: 'employees', component: () => import('@/views/EmployeeView.vue') },
        { path: 'billings', name: 'billings', component: () => import('@/views/BillingView.vue') },
      ],
      beforeEnter: isAuthenticated,
    },
  ],
});

export default router;
