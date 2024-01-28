import UserType from "../types/entityType"

export const isAuthenticated = (user: UserType) => !(user.roles.length === 1 && user.roles[0] === 'ROLE_ANONYMOUS')

export const isAdmin = (user: UserType) => (
  isAuthenticated(user) && 
  user.roles.find(role => role === 'ROLE_ADMIN')
)

export const isVIP = (user: UserType) => (
  isAuthenticated(user) && 
  user.roles.find(role => role === 'ROLE_PRIVILEGED_USER' || role === 'ROLE_ADMIN')
)

export const verboseHighestRole = (user: UserType) => {
  if (user.roles.find(role => role === 'ROLE_ADMIN')) {
    return 'Administrateur'
  }
  if (user.roles.find(role => role === 'ROLE_PRIVILEGED_USER')) {
    return 'Utilisateur VIP'
  }
  if (user.roles.find(role => role === 'ROLE_USER')) {
    return 'Utilisateur'
  }
  return 'Aucun rôle'
}
