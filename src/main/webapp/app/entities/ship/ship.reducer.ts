import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShip, defaultValue } from 'app/shared/model/ship.model';

export const ACTION_TYPES = {
  FETCH_SHIP_LIST: 'ship/FETCH_SHIP_LIST',
  FETCH_SHIP: 'ship/FETCH_SHIP',
  CREATE_SHIP: 'ship/CREATE_SHIP',
  UPDATE_SHIP: 'ship/UPDATE_SHIP',
  DELETE_SHIP: 'ship/DELETE_SHIP',
  RESET: 'ship/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShip>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ShipState = Readonly<typeof initialState>;

// Reducer

export default (state: ShipState = initialState, action): ShipState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHIP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHIP):
    case REQUEST(ACTION_TYPES.UPDATE_SHIP):
    case REQUEST(ACTION_TYPES.DELETE_SHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHIP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHIP):
    case FAILURE(ACTION_TYPES.CREATE_SHIP):
    case FAILURE(ACTION_TYPES.UPDATE_SHIP):
    case FAILURE(ACTION_TYPES.DELETE_SHIP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIP_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHIP):
    case SUCCESS(ACTION_TYPES.UPDATE_SHIP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHIP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/ships';

// Actions

export const getEntities: ICrudGetAllAction<IShip> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHIP_LIST,
    payload: axios.get<IShip>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShip> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHIP,
    payload: axios.get<IShip>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShip> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHIP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShip> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShip> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHIP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
