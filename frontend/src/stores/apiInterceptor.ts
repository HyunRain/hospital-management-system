import axios from 'axios';
import { useAuthStore } from './authStore';

const api = axios.create({
  baseURL: 'http://localhost:8079/api',
  withCredentials: true,
})

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config

    if (error.response?.status === 401 && (error.response?.data?.error  === "Expired Access Token, but refresh token is available") && !originalRequest._retry) {
      originalRequest._retry = true

      const authStore = useAuthStore()
      const refreshed = await authStore.refreshToken()

      if (refreshed) {
        return api(originalRequest)
      }
    }

    return Promise.reject(error)
  },
)

export default api

