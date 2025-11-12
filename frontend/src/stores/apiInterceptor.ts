import axios from 'axios';
import { useAuthStore } from './authStore';
import router from '@/util/functions/routerHelper';

const api = axios.create({
  baseURL: 'http://localhost:8079/api',
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    const authStore = useAuthStore();

    if (
      error.response?.status === 401 &&
      error.response?.data?.error === 'Expired Access Token, but refresh token is available' &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const refreshed = await authStore.refreshToken();

        if (refreshed) {
          return api(originalRequest);
        }
      } catch (error) {
        console.error('Error refreshing token', error);
        authStore.logout();
      }
    }

    if (
      error.response?.status === 401 &&
      error.response?.data?.error === 'Expired Access Token' &&
      router.currentRoute.value.fullPath != '/login'
    ) {
      authStore.logout();
    }

    return Promise.reject(error);
  },
);

export default api;
