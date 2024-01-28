import { FormEvent, useRef, useState } from "react"
import UserType, { SnackMessageType } from "../../../../types/entityType"
import { useMutation } from "react-query"
import { EditUserType } from "../../../../types/queryType"
import { editUserMutation } from "../../../../apis/queries"
import FormError from "../../../FormError"
import { AxiosError } from "axios"

type EditUserModalPropsType = {
  user: UserType,
  closeCallback: () => void
  setSnack: (smt : SnackMessageType) => void,
  refetchUsers: () => void,
}

const EditUserModal: React.FC<EditUserModalPropsType> = ({
  user,
  closeCallback, 
  setSnack,
  refetchUsers
}) => {
  const [email, setEmail] = useState(user.email);
  const [username, setUsername] = useState(user.username)
  const [passwordToUpdate, setPasswordToUpdate] = useState(false)
  const [password, setPassword] = useState('')
  const [pwdLengthValid, setPwdLengthValid] = useState(false)
  const [pwdDigitValid, setPwdDigitValid] = useState(false)
  const [pwdCharValid, setPwdCharValid] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const adminCheckboxRef = useRef<HTMLInputElement>(null)
  const vipUserCheckboxRef = useRef<HTMLInputElement>(null)

  const editUser = async (e : FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const roles = ['ROLE_USER']
    if (adminCheckboxRef.current?.checked) {
      roles.push('ROLE_ADMIN')
    }
    if (vipUserCheckboxRef.current?.checked) {
      roles.push('ROLE_PRIVILEGED_USER')
    }

    const body: EditUserType = {
      username,
      roles
    }

    if (passwordToUpdate) {
      body.password = password
    }
    if (email !== user.email) {
      body.email = email
    }

    editUserMut.mutate(body);
  }

  const updatePassword = (newPassword: string) => {
    setPassword(newPassword)
    setPwdLengthValid(newPassword.length >= 8)
    setPwdCharValid(/[a-z]/i.test(newPassword))
    setPwdDigitValid(/[0-9]/.test(newPassword))
  }

  const handleEditUserSuccess = () => {
    closeCallback()
    setSnack({
      type: 'success',
      message: 'Utilisateur modifié avec succès !'
    })
    refetchUsers()
  }

  const handleEditUserFailure = (error: any) => {
    setErrorMessage(
      error instanceof AxiosError ?
      error.response?.data.error : 
      'Une erreur est survenue.'
    );
  }

  const editUserMut = useMutation(
    async (payload: EditUserType) => {
      return await editUserMutation(user.id, payload)
    },
    {
      onSuccess: handleEditUserSuccess,
      onError: error => handleEditUserFailure(error)
    }
  )

  return (
    <div className="fullscreen-dimmer flex">
      <div className="edit-user-modal flex">
        <div className="top-bar flex">
          <p>Editer l'utilisateur {user.username}</p>
          <i className="fa fa-times" onClick={closeCallback}></i>
        </div>

        { 
          errorMessage !== '' && 
          (
            <div className="error">
              <FormError message={errorMessage} />
            </div>
          )
          
        }
        <form className="edit-user-form flex" onSubmit={e => editUser(e)}>
          <div className="fields flex">
            <div className="field">
              <label htmlFor="email">Email</label>
              <input type="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>

            <div className="field">
              <label htmlFor="username">Pseudonyme</label>
              <input type="text" name="username" value={username} onChange={(e) => setUsername(e.target.value)} required />
            </div>

            <div className="field">
              <div className="flex password-field">
                <label htmlFor="password">Nouveau mot de passe</label>
                <input type="checkbox" onChange={e => setPasswordToUpdate(e.target.checked)} className="password-toggle"/>
              </div>
              <input 
                type="password" 
                name="password" 
                value={password} 
                onChange={(e) => updatePassword(e.target.value)} 
                required={passwordToUpdate} 
                className={`${!passwordToUpdate && 'hidden'}`}
              />
              
            </div>
          </div>
          
          <div className="roles-criterias-container flex">
            <div className="roles-container flex">
              <p>Rôles</p>
              <div className="roles flex">
                <div className="role">
                  <input type="checkbox" ref={adminCheckboxRef} id="admin-role" defaultChecked={user.roles.some(r => r === 'ROLE_ADMIN')}/>
                  <label htmlFor="admin-role">Administrateur</label>
                </div>

                <div className="role">
                  <input type="checkbox" ref={vipUserCheckboxRef} id="vip-role" defaultChecked={user.roles.some(r => r === 'ROLE_PRIVILEGED_USER')}/>
                  <label htmlFor="vip-role">Utilisateur VIP</label>
                </div>

                <div className="role">
                  <input type="checkbox" checked disabled name="std-role" />
                  <label htmlFor="std-role">Utilisateur</label>
                </div>
                
              </div>

            </div>

            <div className={`criterias flex ${!passwordToUpdate && 'hidden'}`}>
              <div className="criteria flex">
                <i className={`fa fa-${pwdLengthValid ? 'check' : 'xmark'}`}></i>
                <p>8 caractères minimum</p>
              </div>

              <div className="criteria flex">
                <i className={`fa fa-${pwdDigitValid ? 'check' : 'xmark'}`}></i>
                <p>Au moins un chiffre</p>
              </div>

              <div className="criteria flex">
                <i className={`fa fa-${pwdCharValid ? 'check' : 'xmark'}`}></i>
                <p>Au moins une lettre</p>
              </div>
            </div>
          </div>
          
          <div className="action-btns flex">
            <button className="cancel" onClick={closeCallback}>
              Annuler
            </button>
            <button 
              type="submit"
              className="validate"
              disabled={!(
                (
                  !passwordToUpdate || 
                  (
                    pwdLengthValid &&
                    pwdCharValid &&
                    pwdDigitValid
                  )
                ) &&
                email !== '' && 
                username !== ''
              )}
            >
              Enregistrer
            </button> 
          </div>
        </form>
      </div>
    </div>
  )
}

export default EditUserModal