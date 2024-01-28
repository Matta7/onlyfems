import React, { useState } from "react";
import { useMutation } from "react-query";
import { LoginType } from "../../../types/queryType";
import { loginMutation } from "../../../apis/queries";
import FormError from "../../FormError";
import { useNavigate } from "react-router-dom";
import clientPath from "../../../utils/clientPath";

type LoginFormPropsTypes = {
  setIsLoading: (state: boolean) => void,
};

const LoginForm: React.FC<LoginFormPropsTypes> = ({ 
  setIsLoading,
}) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();
  
  const handleLoginSuccess = () => {
    setIsLoading(false);
    navigate(clientPath.GALLERY)
  }

  const handleLoginFailure = () => {
    setIsLoading(false);
    setErrorMessage('Erreur : email ou mot de passe incorrect');
  }

  const loginMut = useMutation(
    async (payload: LoginType) => {
      return await loginMutation(payload);
    },
    {
      onSuccess: handleLoginSuccess,
      onError: handleLoginFailure
    }
  )

  const login = (e : React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    loginMut.mutate({email, password} as LoginType);
    setIsLoading(true);
  }

  return (
    <div className="flex">
    {
      errorMessage !== '' ?
      <FormError message={errorMessage} /> :
      null
    }
    <div className="login-form-container flex">
      <p className="form-title">Connexion</p>
        <form className="login-form flex" action="" onSubmit={e => login(e)}>
          <label htmlFor="email">Email</label>
          <input type="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

          <label htmlFor="password">Mot de passe</label>
          <input type="password" name="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          
          <button type="submit">Se connecter</button>
        </form>
    </div>
    </div>
  )
}

export default LoginForm;