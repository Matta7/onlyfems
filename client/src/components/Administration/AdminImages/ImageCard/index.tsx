import { useMutation } from "react-query";
import { ImageType, SnackMessageType } from "../../../../types/entityType"
import { EditImageType } from "../../../../types/queryType";
import { editImageMutation } from "../../../../apis/queries";
import { useRef, useState } from "react";
import { AxiosResponse } from "axios";

type ImageCardPropsType = {
  image: ImageType,
  editCallback: () => void,
  deleteCallback: () => void,
  setSnack: (smt: SnackMessageType) => void,
}

const ImageCard: React.FC<ImageCardPropsType> = ({
  image,
  editCallback,
  deleteCallback,
  setSnack,
}) => {
  const [usedImage, setUsedImage] = useState<ImageType>(image)
  const privacyLabelRef = useRef<HTMLSpanElement>(null)
  const extension = usedImage.name.split('.').pop();

  const handlePrivacyToggleSuccess = (e: AxiosResponse) => {
    setSnack({
      type: 'success',
      message: `L'image est désormais ${usedImage.public ? 'privée' : 'publique'} !`
    })
    setUsedImage(e.data as ImageType)
  }

  const handlePrivacyToggleFailure = () => {
    setSnack({
      type: 'error',
      message: 'Une erreur est survenue lors de l\'édition de l\'image'
    })
  }

  const togglePrivacyMut = useMutation(
    async (payload: EditImageType) => {
      return await editImageMutation(usedImage.id, payload)
    },
    {
      onSuccess: e => handlePrivacyToggleSuccess(e),
      onError: handlePrivacyToggleFailure
    }
  )

  const togglePrivacy = () => {
    togglePrivacyMut.mutate({
      privacy: !usedImage.public
    })
  }

  const download = () => {
    let anchor = document.createElement("a")
    anchor.href = `data:image/png;base64,${usedImage.base64}`
    anchor.download = usedImage.name
    document.body.appendChild(anchor)
    anchor.click()
    document.body.removeChild(anchor)
  }

  return (
    <div className="image-card flex">
      <div className="image-container flex">
        <img loading="lazy" src={`data:image/${extension};base64, ${usedImage.base64}`} alt={usedImage.name} />
      </div>
      <p className="image-name">{usedImage.name}</p>
      <div className="overlay flex">
        <span ref={privacyLabelRef} className={`privacy ${usedImage.public ? 'inactive' : ''}`}>Privée</span>
        <div className="actions flex">
          <i className={`fa fa-lock${usedImage.public ? '' : '-open'}`} onClick={togglePrivacy}></i>
          <i className="fa fa-pen" onClick={editCallback}></i>
          <i className="fa fa-download" onClick={download}></i>
          <i className="fa fa-trash" onClick={deleteCallback}></i>
        </div>
        <p className="description">{usedImage.description}</p>
      </div>
    </div>
  )
}

export default ImageCard