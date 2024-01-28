import UserType from "../types/entityType";
import { AllImagesType, AllUsersType, EditImageType, EditUserType, LoginType, NewImageType, NewUserType } from "../types/queryType";
import apiRoute from "../utils/apiRoute";
import api from "./api";


// ****************************************************************************
// Authentication
// ****************************************************************************

export const loginMutation = async (credentials: LoginType) => {
  const res = await api.post(
    apiRoute.AUTH_LOGIN,
    credentials,
    {
      withCredentials: true
    }
  )
  return res;
}

export const logoutQuery = async () => {
  const res = await api.get(
    apiRoute.AUTH_LOGOUT,
    {
      withCredentials: true
    }
  )
  return res;
}

export const currentUserQuery = async () => {
  const { data } = await api.get<UserType>(
    apiRoute.AUTH_USER,
    {
      withCredentials: true
    }
  )
  return data
}

// ****************************************************************************
// Users
// ****************************************************************************

export const newUserMutation = async (newUserPayload: NewUserType) => {
  const res = await api.post(
    apiRoute.USERS,
    newUserPayload,
    {
      withCredentials: true
    }
  )
  return res;
}

export const editUserMutation = async (
  userId: number,
  editUserPayload: EditUserType
) => {
  const res = await api.patch(
    `${apiRoute.USERS}/${userId}`,
    editUserPayload,
    {
      withCredentials: true
    }
  )
  return res;
}

export const allUsersQuery = async () => {
  const { data } = await api.get<AllUsersType>(
    apiRoute.USERS,
    {
      withCredentials: true
    }
  )
  return data;
}

export const deleteUserQuery = async (userId: number) => {
  const res = await api.delete(
    `${apiRoute.USERS}/${userId}`,
    {
      withCredentials: true
    }
  )
  return res;
}

// ****************************************************************************
// Images
// ****************************************************************************

export const newImageMutation = async (newImagePayload: NewImageType) => {
  const res = await api.post(
    apiRoute.IMAGES,
    newImagePayload,
    {
      withCredentials: true
    }
  )
  return res;
}

export const editImageMutation = async (
  imageId: number,
  editImagePayload: EditImageType
) => {
  const res = await api.patch(
    `${apiRoute.IMAGES}/${imageId}`,
    editImagePayload,
    {
      withCredentials: true
    }
  )
  return res;
}

export const allImagesQuery = async (
  publicity: boolean,
  page: number,
  pageSize: number
) => {
  const { data } = await api.get<AllImagesType>(
    apiRoute.IMAGES,
    {
      params: {
        publicity: !publicity, //Un peu tricky, mais étant donné que si on veut les images VIP, on doit envoyer "false" vu que on veut les images avec la publicity "false"/privée
        page: page,
        size: pageSize
      },
      withCredentials: true
    }
  )
  return data;
}

export const deleteImageQuery = async (imageId: number) => {
  const res = await api.delete(
    `${apiRoute.IMAGES}/${imageId}`,
    {
      withCredentials: true
    }
  )
  return res;
}
