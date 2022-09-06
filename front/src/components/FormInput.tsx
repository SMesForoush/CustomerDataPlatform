import classNames from 'classnames';
import { InputHTMLAttributes } from 'react';
import { Control, ControllerRenderProps, FieldPath, useController } from 'react-hook-form';

export type FormInputWithControllerProps<T, TName extends FieldPath<T>> = InputHTMLAttributes<HTMLInputElement> & {
  name: TName;
  control: Control<T>;
  labelName?: string;
  render?: (field: ControllerRenderProps<T, TName>) => JSX.Element
  horizental?: boolean
};

export function FormInputWithController<T, TName extends FieldPath<T>>({
  className = '',
  control,
  labelName = '',
  render,
  horizental,
  ...props
}: FormInputWithControllerProps<T, TName>): JSX.Element {
  const {
    field,
    formState: { errors }
  } = useController({
    name: props.name,
    control
  });
  return (
    <label className={classNames(
      "space-x-2 space-y-2",
      {
        "flex": horizental
      }
    )}>
      {labelName && <div>{labelName}</div>}
      {render && render(field)}
      {!render && <input className={classNames(className, "bg-blue-50")} {...(field as any)} {...props} />}
      {errors[props.name]?.message && <div>{errors[props.name]?.message as any}</div>}
    </label>
  );
}
