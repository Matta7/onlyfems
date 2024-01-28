import { FormEvent, useRef, useState } from "react"
import { SnackMessageType } from "../../../../types/entityType"
import { useMutation } from "react-query"
import { NewUserType } from "../../../../types/queryType"
import { newUserMutation } from "../../../../apis/queries"
import FormError from "../../../FormError"

type NewUserModalPropsType = {
  closeCallback: () => void
  setSnack: (smt : SnackMessageType) => void,
  refetchUsers: () => void,
}

const NewUserModal: React.FC<NewUserModalPropsType> = ({
  closeCallback, 
  setSnack,
  refetchUsers,
}) => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [pwdLengthValid, setPwdLengthValid] = useState(false)
  const [pwdDigitValid, setPwdDigitValid] = useState(false)
  const [pwdCharValid, setPwdCharValid] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const adminCheckboxRef = useRef<HTMLInputElement>(null)
  const vipUserCheckboxRef = useRef<HTMLInputElement>(null)

  const addNewUser = async (e : FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const roles = ['ROLE_USER']
    if (adminCheckboxRef.current?.checked) {
      roles.push('ROLE_ADMIN')
    }
    if (vipUserCheckboxRef.current?.checked) {
      roles.push('ROLE_PRIVILEGED_USER')
    }

    newUserMut.mutate({ email, username, password, roles});
  }

  const updatePassword = (newPassword: string) => {
    setPassword(newPassword)
    setPwdLengthValid(newPassword.length >= 8)
    setPwdCharValid(/[a-z]/i.test(newPassword))
    setPwdDigitValid(/[0-9]/.test(newPassword))
  }

  const handleNewUserSuccess = () => {
    closeCallback()
    setSnack({
      type: 'success',
      message: 'Utilisateur créé avec succès !'
    })
    refetchUsers()
  }

  const handleNewUserFailure = () => {
    setErrorMessage('Adresse email déjà utilisée !');
  }

  const newUserMut = useMutation(
    async (payload: NewUserType) => {
      return await newUserMutation(payload)
    },
    {
      onSuccess: handleNewUserSuccess,
      onError: handleNewUserFailure
    }
  )

  return (
    <div className="fullscreen-dimmer flex">
      <div className="new-user-modal flex">
        <div className="top-bar flex">
          <p>Nouvel utilisateur</p>
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
        <form className="new-user-form flex" onSubmit={e => addNewUser(e)}>
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
              <label htmlFor="password">Mot de passe</label>
              <input type="password" name="password" value={password} onChange={(e) => updatePassword(e.target.value)} required />
            </div>
          </div>
          
          <div className="roles-criterias-container flex">
            <div className="roles-container flex">
              <p>Rôles</p>
              <div className="roles flex">
                <div className="role">
                  <input type="checkbox" ref={adminCheckboxRef} id="admin-role" />
                  <label htmlFor="admin-role">Administrateur</label>
                </div>

                <div className="role">
                  <input type="checkbox" ref={vipUserCheckboxRef} id="vip-role" />
                  <label htmlFor="vip-role">Utilisateur VIP</label>
                </div>

                <div className="role">
                  <input type="checkbox" checked disabled name="std-role" />
                  <label htmlFor="std-role">Utilisateur</label>
                </div>
                
              </div>

            </div>

            <div className="criterias flex">
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
                pwdLengthValid &&
                pwdCharValid &&
                pwdDigitValid &&
                email !== '' && 
                username !== ''
              )}
            >
              Ajouter
            </button> 
          </div>
          
        </form>
      </div>
    </div>
  )
}

export default NewUserModal