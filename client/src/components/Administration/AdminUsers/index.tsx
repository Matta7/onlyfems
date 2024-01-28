import React, { useState } from "react"

import '../../../styles/Admin/Users.css'
import UserType, { SnackMessageType } from "../../../types/entityType"
import { useQuery } from "react-query"
import { allUsersQuery, deleteUserQuery } from "../../../apis/queries"
import LoadingCircle from "../../LoadingCircle"
import { verboseHighestRole } from "../../../utils/user"
import NewUserModal from "./NewUserModal"
import EditUserModal from "./EditUserModal"
import useGetUser from "../../../hooks/useGetUser"

type AdminUsersPropsType = {
  setSnack: (snackMessage: SnackMessageType) => void
}

const AdminUsers: React.FC<AdminUsersPropsType> = ({
  setSnack,
}) => {
  const [showNewUserModal, setShowNewUserModal] = useState(false)
  const [showEditUserModal, setShowEditUserModal] = useState(false)
  const [userToEdit, setUserToEdit] = useState<UserType | null>(null)

  const { user } = useGetUser()

  const { data: allUsers, refetch: refetchAllUsers } = useQuery<UserType[]>({
    queryKey: ['all-users'],
    queryFn: async () => {
      const { users } = await allUsersQuery()
      return users
    }
  })

  if (!allUsers || !user) {
    return (
      <div className="empty flex">
        <LoadingCircle />
      </div>
    ) 
  }

  const editUser = (user: UserType) => {
    setShowEditUserModal(true)
    setUserToEdit(user)
  }
  
  const deleteUser = async (userToDelete: UserType) => {
    if (userToDelete.id === user.id && userToDelete.email === user.email) {
      setSnack({
        type: 'warning',
        message: 'Vous ne pouvez pas vous supprimer vous-même !',
      })
      return
    } 
    await deleteUserQuery(userToDelete.id);
    refetchAllUsers()
    setSnack({
      type: 'success',
      message: 'Utilisateur supprimé avec succès !',
    })
  }

  const renderedUsers = allUsers.map(
    user => (
      <tr key={user.id}>
        <td>{user.username}</td>
        <td>{user.email}</td>
        <td>{verboseHighestRole(user)}</td>
        <td>
          <div className="actions flex">
            <i className="fa fa-pen" onClick={() => editUser(user)}></i>
            <i className="fa fa-trash" onClick={() => deleteUser(user)}></i>
          </div>
        </td>
      </tr>
    )
  )

  return (
    <div className="admin-users flex">
      {
        showNewUserModal ?
        <NewUserModal closeCallback={() => setShowNewUserModal(false)} setSnack={setSnack} refetchUsers={refetchAllUsers} /> :
        null
      }
      {
        showEditUserModal ?
        <EditUserModal closeCallback={() => setShowEditUserModal(false)} setSnack={setSnack} user={userToEdit as UserType} refetchUsers={refetchAllUsers} /> :
        null
      }

      <div className="show-modal-btn-container">
        <button 
          className="show-modal-btn" 
          onClick={() => setShowNewUserModal(true)}
        >
          Nouvel utilisateur
        </button>
      </div>
      
      <div className="users flex">
        <table>
          <thead>
            <tr>
              <th>Pseudonyme</th>
              <th>Email</th>
              <th>Plus haut rôle</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {renderedUsers}
          </tbody>
        </table> 
      </div>     
    </div>
  )
}

export default AdminUsers