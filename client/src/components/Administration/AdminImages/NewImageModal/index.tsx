import { ChangeEvent, FormEvent, useRef, useState } from "react";
import { useMutation } from "react-query";
import { NewImageType } from "../../../../types/queryType";
import { newImageMutation } from "../../../../apis/queries";
import { toBase64 } from "../../../../utils/file";
import { SnackMessageType } from "../../../../types/entityType";

type NewImageModalPropsType = {
  closeCallback: () => void,
  setSnack: (smt: SnackMessageType) => void,
  refetch: () => void,
}

const NewImageModal: React.FC<NewImageModalPropsType> = ({
  closeCallback,
  setSnack,
  refetch,
}) => {
  const [image, setImage] = useState<File | null>(null);
  const [description, setDescription] = useState('');

  const newImageRef = useRef<HTMLInputElement>(null);
  const privacyRef = useRef<HTMLInputElement>(null);

  const handleNewImage = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setImage(e.target.files[0])
    }
  }

  const addNewImage = async (e : FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    if (!image) {
      return
    }
    const isPrivate = privacyRef.current?.checked as boolean;
    const base64image =  await toBase64(image) as string
    newImageMut.mutate({
      description,
      privacy: !isPrivate,
      base64: base64image.split(',')[1],
      name: image.name,
      contentType: image.type
    })
    
  }

  const handleNewImageSuccess = () => {
    setSnack({
      type: 'success',
      message: 'Image ajoutée avec succès !'
    })
    closeCallback()
    refetch()
  }

  const handleNewImageFailure = () => {
    setSnack({
      type: 'error',
      message: 'Une erreur est survenue lors de l\'ajout de l\'image'
    })
  }

  const newImageMut = useMutation(
    async (payload: NewImageType) => {
      return await newImageMutation(payload)
    },
    {
      onSuccess: handleNewImageSuccess,
      onError: handleNewImageFailure
    }
  )

  return (
    <div className="fullscreen-dimmer flex">
      <div className="new-image-modal flex">
        <div className="top-bar flex">
          <p>Nouvelle image</p>
          <i className="fa fa-times" onClick={closeCallback}></i>
        </div>

        <form className="new-image-form flex" onSubmit={e => addNewImage(e)}>
          <div className="field image-input flex">
            <input
              ref={newImageRef}
              className="new-image-input"
              onChange={e => handleNewImage(e)}
              type="file"
              accept="image/png, image/jpeg, image/jpg, image/svg, image/gif" 
            />
            <button
              className="import-image-button"
              onClick={() => newImageRef.current?.click()}
            >
              Importer une image
            </button>
            {
              image ? 
              <p className="image-name">{image.name}</p> :
              null
            } 
          </div>

          <div className="field">
            <label htmlFor="description">Description</label>
            <textarea
              name="description"
              className="new-image-description"
              value={description}
              rows={10}
              cols={50}
              onChange={e => setDescription(e.target.value)}
            ></textarea>
          </div>

          <div className="field flex privacy-field">
            <label htmlFor="privacy">Privée</label>
            <input
              name="privacy"
              className="new-image-privacy"
              type="checkbox"
              ref={privacyRef}
            ></input>
          </div>

          <div className="buttons flex">
            <button
              className="cancel"
              onClick={closeCallback}
            >
              Annuler
            </button>
            <button
              type="submit"
              className="upload"
              disabled={!image}
            >
              Ajouter
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default NewImageModal