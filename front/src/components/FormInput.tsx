import classNames from 'classnames';
import { InputHTMLAttributes } from 'react';
import { Control, ControllerRenderProps, useController } from 'react-hook-form';

export type FormInputWithControllerProps = InputHTMLAttributes<HTMLInputElement> & {
  name: string;
  control: Control<any>;
  labelName?: string;
  render?: (field: ControllerRenderProps) => JSX.Element
};

export function FormInputWithController({
  className = '',
  control,
  labelName = '',
  render,
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
      {render && render(field)}
      {!render && <input className={classNames(className, "bg-blue-50")} {...field} {...props} />}
      {errors[props.name]?.message && <div>{errors[props.name]?.message.toString()}</div>}
    </label>
  );
}
