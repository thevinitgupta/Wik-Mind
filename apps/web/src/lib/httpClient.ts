import Axios from "axios";

let refreshPromise: Promise<void> | null = null;

const httpClient = Axios.create({
  baseURL: process.env.NEXT_PUBLIC_BASE_URL!,
  headers: {
    "X-Requested-With": "XMLHttpRequest",
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  withCredentials: true,
});

export const backendClient = Axios.create({
  baseURL: process.env.NEXT_PUBLIC_BASE_URL!,
  headers: {
    "X-Requested-With": "XMLHttpRequest",
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  withCredentials: true,
});
backendClient.interceptors.response.use(
  response => response,
  async (error) => {
    const originalRequest = error.config;

    const isRefreshRoute = originalRequest.url?.includes("/api/auth/refresh");

    if (error.response?.status === 401 && isRefreshRoute) {
      console.log("Refresh route failed")
      refreshPromise = null;
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      if (!refreshPromise) {
        refreshPromise = backendClient
          .post("/api/auth/refresh")
          .then(() => {
            refreshPromise = null;
          })
          .catch((refreshError) => {
            refreshPromise = null;
            return Promise.reject(refreshError);
          });
      }

      try {
        // Wait globally for the active single refresh loop to execute
        await refreshPromise;

        return backendClient(originalRequest);
      } catch (retryError) {
        return Promise.reject(retryError);
      }
    }
    return Promise.reject(error);
  }
);

export default httpClient;
