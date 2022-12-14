import React, {
  Dispatch,
  Reducer,
  ReducerAction,
  ReducerState,
  useContext,
  useEffect,
  useReducer,
  useState
} from 'react';

import { IAuthInfo } from '../types/auth.types';

const initialState: IAuthInfo = undefined;

type ReducerType = Reducer<undefined | IAuthInfo, IAction>;
type ContextType = [ReducerState<ReducerType>, Dispatch<ReducerAction<ReducerType>>];
export const AuthStateContext = React.createContext<ContextType>([undefined, () => undefined]);

export enum ActionType {
  SetDetails = 'setAuthDetails',
  RemoveDetails = 'removeAuthDetails'
}

interface IAction {
  type: ActionType;
  payload?: IAuthInfo;
}

const reducer: React.Reducer<IAuthInfo, IAction> = (state, action) => {
  switch (action.type) {
    case ActionType.SetDetails:
      return {
        email: action.payload.email,
        name: action.payload.name
      };
    case ActionType.RemoveDetails:
      return initialState;
    default:
      throw new Error(`Unhandled action type: ${action.type}`);
  }
};

export const AuthProvider = ({ children }: any) => {
  let localState = null;
  if (
    typeof localStorage !== 'undefined' &&
    localStorage.getItem('userInfo') &&
    localStorage.getItem('userInfo') !== 'undefined'
  ) {
    localState = JSON.parse(localStorage.getItem('userInfo') || '');
  }
  const [state, dispatch] = useReducer<ReducerType>(reducer, localState || initialState);

  if (typeof localStorage !== 'undefined') {
    useEffect(() => {
      localStorage.setItem('userInfo', JSON.stringify(state));
    }, [state]);
  }
  return (
    <AuthStateContext.Provider value={[state, dispatch]}>{children}</AuthStateContext.Provider>
  );
};

export const useAuth: () => ContextType = () => {
  const [firstRender, setFirstRender] = useState(true);
  useEffect(() => {
    setFirstRender(false);
  }, []);
  const [state, setState] = useContext(AuthStateContext);
  return firstRender ? [undefined, setState] : [state, setState];
};
