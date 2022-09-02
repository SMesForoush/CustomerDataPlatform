import { InputHTMLAttributes } from 'react';
import { Control, useController } from 'react-hook-form';

export type FormInputWithControllerProps = InputHTMLAttributes<HTMLInputElement> & {
  name: string;
  control: Control<any>;
  kind?: 'input' | 'textarea';
  labelName?: string;
};

export function FormInputWithController({
  className = '',
  kind = 'input',
  control,
  labelName = '',
  ...props
}: FormInputWithControllerProps): JSX.Element {
  const {
    field,
    formState: { errors }
  } = useController({
    name: props.name,
    control
  });
  return (
    <label>
      {labelName && <div>{labelName}</div>}
      <input className={className} {...field} {...props} />
      {errors[props.name]?.message && <div>{errors[props.name]?.message.toString()}</div>}
    </label>
  );
}
