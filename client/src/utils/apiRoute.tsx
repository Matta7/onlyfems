const AUTH_BASE = '/authentication'

const apiRoute = {
  // Get the current user.
  AUTH_USER: `${AUTH_BASE}/user`,
  // Logs in the user.
  AUTH_LOGIN: `${AUTH_BASE}/login`,
  // Logs out the user.
  AUTH_LOGOUT: `${AUTH_BASE}/logout`,
  // Images
  IMAGES: '/images',
  // Users
  USERS: '/users',
}

export default apiRoute