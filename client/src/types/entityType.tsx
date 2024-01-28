/**
 * User type.
 */
export default interface UserType {
  id: number,
  email: string,
  username: string
  roles: string[]
}
/**
 * Snack message type.
 */
export type SnackMessageType = {
  type: 'success' | 'info' | 'warning' | 'error',
  fullTop?: boolean,
  message: string
}

export type ImageType = {
  id: number,
  name: string,
  description: string,
  public: boolean, 
  base64: string
}