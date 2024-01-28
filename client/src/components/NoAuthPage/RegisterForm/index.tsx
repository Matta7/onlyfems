import React, { useState } from "react";
import { useMutation } from "react-query";
import { NewUserType } from "../../../types/queryType";
import { newUserMutation } from "../../../apis/queries";
import { SnackMessageType } from "../../../types/entityType";
import FormError from "../../FormError";

type RegisterFormPropsTypes = {
  removeRegisterCallback: () => void,
  putLoginCallback: () => void,
  setIsLoading: (state: boolean) => void,
  setSnack: (smt: SnackMessageType) => void
};

const RegisterForm: React.FC<RegisterFormPropsTypes> = ({
  removeRegisterCallback,
  putLoginCallback,
  setIsLoading,
  setSnack,
}) => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [pwdLengthValid, setPwdLengthValid] = useState(false);
  const [pwdDigitValid, setPwdDigitValid] = useState(false);
  const [pwdCharValid, setPwdCharValid] = useState(false);

  const handleRegisterSuccess = () => {
    setIsLoading(false);
    removeRegisterCallback()
    putLoginCallback()
    setSnack({
      type: 'success',
      fullTop: true,
      message: 'Inscription réalisée avec succès'
    })
  }

  const handleRegisterFailure = () => {
    setIsLoading(false);
    setErrorMessage('Adresse email déjà utilisée !');
  }

  const registerMut = useMutation(
    async (payload: NewUserType) => {
      return await newUserMutation(payload);
    },
    {
      onSuccess: handleRegisterSuccess,
      onError: handleRegisterFailure
    }
  )

  const register = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    registerMut.mutate({ email, username, password, roles: ['ROLE_USER']} as NewUserType);
    setIsLoading(true);
  }

  const updatePassword = (newPassword: string) => {
    setPassword(newPassword)
    setPwdLengthValid(newPassword.length >= 8)
    setPwdCharValid(/[a-z]/i.test(newPassword))
    setPwdDigitValid(/[0-9]/.test(newPassword))
  }

  return (
    <div className="flex">
      {
        errorMessage !== '' ?
        <FormError message={errorMessage} /> :
        null
      }
    
      <div className="register-form-container flex">
        <p className="form-title">Inscription</p>
        <form className="register-form flex" onSubmit={e => register(e)}>
          <label htmlFor="email">Email</label>
          <input type="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

          <label htmlFor="username">Pseudonyme</label>
          <input type="text" name="username" value={username} onChange={(e) => setUsername(e.target.value)} required />

          <label htmlFor="password">Mot de passe</label>
          <input type="password" name="password" value={password} onChange={(e) => updatePassword(e.target.value)} required />

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
          
          <button 
            type="submit"
            disabled={!(
              pwdLengthValid &&
              pwdCharValid &&
              pwdDigitValid &&
              email !== '' && 
              username !== ''
            )}
          >
            S'inscrire
          </button>
        </form>
      </div>
    </div>
  )
};

export default RegisterForm;