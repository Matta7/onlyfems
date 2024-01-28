import React from "react"

type FormErrorPropsType = {
  message: string,
}

const FormError: React.FC<FormErrorPropsType> = ({
  message,
}) => {
  return (
    <div className="form-error">
      <p>{message}</p>
    </div>
  )
}

export default FormError